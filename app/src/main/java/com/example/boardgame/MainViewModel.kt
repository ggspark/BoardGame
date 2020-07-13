import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val N = 8

class MainViewModel : ViewModel() {

    //Keep track of current board
    private val _board = MutableLiveData<IntArray>()
    val board: LiveData<IntArray>
        get() = _board

    //Keep track of dice value
    private val _dice = MutableLiveData<Int>()
    val dice: LiveData<Int>
        get() = _dice


    //Keep track of current token
    private var _token = MutableLiveData<Int>()
    val token: LiveData<Int>
        get() = _token

    /**
     * Function called when dice rolled
     */
    fun diceRolled() {
        //Select the next dice value
        val diceValue = (1..6).random()
        //Update dice value
        _dice.value = diceValue
        //Compute new token position
        _token.value = findNextPosition(_board.value!!, _token.value!!.plus(diceValue))
    }

    /**
     * Recursive function to find the next position of token
     */
    private fun findNextPosition(array: IntArray, curPos: Int): Int {
        if (curPos < 0) {
            return 0
        }
        if (curPos >= array.size) {
            return array.size - 1
        }

        if (array[curPos] == 0) {
            return curPos
        }
        return findNextPosition(array, curPos + array[curPos])
    }


    /**
     * Initialise the board and view model
     */
    init {
        //Create a single dimension array of N*N size
        val array = IntArray(N * N)
        //Initialise the array with random values or 0 with a 50-50 probability
        for (i in 0 until (array.size)) {
            //We can tweak this to increase or decrease the random to zero ratio
            val keepZero = (0..1).random()
            if (keepZero == 0 || previous5Random(i, array)) {
                array[i] = 0
            } else {
                array[i] = (-5..5).random()
            }
        }
        array[0] = 0 //Set start as 0
        array[N * N - 1] = 0 //Set end as 0
        removeLoops(array) //Remove edge cases
        //Initialise values
        _board.value = array
        _token.value = 0
        _dice.value = 0
    }

    /**
     * Find if previous 5 values are not 0 to prevent dead hole
     * This will prevent the condition where 0,-1,-2,-3,-4,-5 are consequent and keep the token
     * stuck at position 0
     */
    private fun previous5Random(pos: Int, array: IntArray): Boolean {
        if (pos < 5) {
            return false
        }
        for (i in pos - 5 until pos) {
            if (array[i] == 0) {
                return false
            }
        }
        return true
    }

    /**
     * Function to remove edge cases like exceeding bounds or cycles in the board
     */
    private fun removeLoops(array: IntArray) {
        for (i in 0 until (array.size)) {
            //Mark 0 if exceeding bounds
            val next = i + array[i]
            if (next < 0 || next >= array.size) {
                array[i] = 0
            }
            //Mark 0 if making a loop
            var slow = i
            var fast = i
            while (fast >= 0 && fast < array.size && (fast + array[fast]) >= 0 && (fast + array[fast]) < array.size) {
                slow += array[slow]
                fast += array[fast]
                fast += array[fast]
                if (slow == fast) {
                    array[slow] = 0
                    break
                }
            }
        }
    }

}