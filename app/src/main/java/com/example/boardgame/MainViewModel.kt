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
    private var _token = 0
    val token: Int
        get() = _token

    fun diceRolled() {
        val diceValue = (1..6).random()
        //Update dice value
        _dice.value = diceValue
        //Compute new token position
        _token = findNextPosition(_board.value!!, _token + diceValue)
        //Update board
        _board.value = _board.value
    }


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


    init {
        val array = IntArray(N * N)
        for (i in 0 until (array.size)) {
            val keepZero = (0..1).random()
            if (keepZero == 0) {
                array[i] = (-5..5).random()
            } else {
                array[i] = 0
            }
        }
        array[0] = 0
        array[N * N - 1] = 0
        removeLoops(array)

        _token = 0
        _dice.value = 0
        _board.value = array
    }

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