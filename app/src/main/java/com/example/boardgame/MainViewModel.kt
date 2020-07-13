import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val N = 8

class MainViewModel : ViewModel() {

    //Keep track of current word count
    private val _array = MutableLiveData<IntArray>()
    val array: LiveData<IntArray>
        get() = _array

    init {
        val array = IntArray(N * N)
        for (i in 0 until (array.size)) {
            array[i] = (-5..5).random()
        }
        array[0] = 0
        array[N * N - 1] = 0
        removeLoops(array)
        _array.value = array
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