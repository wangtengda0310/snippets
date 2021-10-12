
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * @author 王腾达 446453149@qq.com
 */
public class ListBitMapTest {

    @Test
    public void valuedBits() {
        assertEquals("有值的位数", 1, ListBitMap.valuedBits(8));
        assertEquals("有值的位数", 2, ListBitMap.valuedBits(10));
    }

    @Test
    public void getData() {
        ListBitMap bitMap = new ListBitMap();
        bitMap.set(1, true);
        bitMap.set(3, true);
        bitMap.set(64, true);

        List<Long> data = bitMap.getData();
        assertEquals(2, data.size());
        assertEquals(10, (long)data.get(0));
        assertEquals(1, (long)data.get(1));

        // 不会缩容
        bitMap.set(3, false);
        data = bitMap.getData();
        assertEquals(2, data.size());

        // 缩容
        bitMap.set(64, false);
        data = bitMap.getData();
        assertEquals(1, data.size());
        assertEquals(2, (long)data.get(0));

        // 缩容
        bitMap.set(1, false);
        data = bitMap.getData();
        assertEquals(0, data.size());

        bitMap.set(10000000, true);
        data = bitMap.getData();
        assertEquals(156251, data.size());
    }

    @Test
    public void testBitMap() {
        ListBitMap bitMap = new ListBitMap();
        bitMap.set(1, true);
        bitMap.set(3, true);
        assertEquals("容量", 64, bitMap.bits());
        assertEquals("有值的位数", 2, bitMap.valuedBits());
        assertEquals("节点数", 1, bitMap.getData().size());

        bitMap.set(64, true);
        assertEquals("节点数", 2, bitMap.getData().size());

        assertTrue(bitMap.get(1));
        assertTrue(bitMap.get(3));
        assertTrue(bitMap.get(64));

        bitMap.set(1, false);
        bitMap.set(3, false);
        bitMap.set(64, false);
        assertFalse(bitMap.get(1));
        assertFalse(bitMap.get(3));
        assertFalse(bitMap.get(64));
    }

}