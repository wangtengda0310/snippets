
import java.util.ArrayList;
import java.util.List;

/**
 * 用List模拟位数据
 * 非线程安全
 * 对低位没数据,超高位有数据的情况不友好
 * @author 王腾达 446453149@qq.com
 */
public class ListBitMap {
    public static final int LONG_VALUE_BITS = Long.SIZE;
    private List<Long> data = new ArrayList<>();

    /** 按位设值 会扩容缩容 */
    public void set(int bit, boolean value) {
        int index = bit / LONG_VALUE_BITS;
        int indexBit = bit % LONG_VALUE_BITS;
        // 扩容
        if (index >= data.size()) {
            int size = data.size();
            List<Long> newList = new ArrayList<>(index+1);
            newList.addAll(data);
            data = newList;
            for (int i = size; i < index+1; i++) {
                data.add(0L);
            }
        }
        Long aLong = data.get(index);
        long l;
        if (value) {
            l = aLong | (1L << indexBit);
        } else {
            l = aLong - (1L << indexBit);
        }
        data.set(index, l);
        // 缩容
        while (data.size()>0&&data.get(data.size() - 1) == 0) {
            data.remove(data.size() - 1);
        }
    }

    /** 按位取值 */
    public boolean get(int bit) {
        if (data.size() == 0) {
            return false;
        }
        int index = bit / LONG_VALUE_BITS;
        int indexBit = bit % LONG_VALUE_BITS;
        if (index > data.size()) {
            return false;
        }
        Long aLong = data.get(index);
        return (aLong | (1L << indexBit)) > 0;
    }

    /** 容量
     * @return  {@link #LONG_VALUE_BITS}的整数倍*/
    public long bits() {
        return (long) data.size() * LONG_VALUE_BITS;
    }

    /** 统计有值的位数 */
    public long valuedBits() {
        return data.stream().reduce(0L, (v1, v2)->valuedBits(v1)+valuedBits(v2));
    }

    /** 统计有值的位数 */
    public static long valuedBits(long number) {
        long count = 0;
        if (number == 0) {
            return 0;
        }
        for (int i = 0; i <LONG_VALUE_BITS ; i++) {
            if (((number >>>= i)&1) > 0) {
                count++;
            }
        }
        return count;
    }

    /** 底层原始数据的getter 不要引用这个返回值不然扩容后可能内存泄漏 */
    public List<Long> getData() {
        return data;
    }

    /** 底层原始数据的setter */
    public void setData(List<Long> data) {
        this.data = data;
    }
}
