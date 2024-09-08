package heraclius.core.dict

// 字典键值对
class Dict(private val dataMap: MutableMap<Symbols.Symbol<*>, Any> = mutableMapOf()) {
    // 获取键值对，未找到则返回null
    fun <V> valueOrNull(key: Symbols.Symbol<V>): V? {
        @Suppress("UNCHECKED_CAST")
        return this.dataMap[key] as V?
    }

    // 获取键值对，未找到则返回默认值
    fun <V> value(key: Symbols.Symbol<V>, default: V): V {
        if (!dataMap.containsKey(key)) return default
        @Suppress("UNCHECKED_CAST")
        return this.dataMap[key] as V
    }

    // 使用hashMap作为该对象的hashcode
    override fun hashCode(): Int {
        return dataMap.hashCode()
    }

    // 使用hashMap的toString
    override fun toString(): String {
        return dataMap.toString()
    }

    // 获取键值对
    operator fun <V> get(key: Symbols.Symbol<V>): V {
        val result = this.dataMap[key] ?: throw RuntimeException("DataStoreKey not found")
        @Suppress("UNCHECKED_CAST")
        return result as V
    }

    // 设置键值对
    operator fun <V> set(key: Symbols.Symbol<V>, value: V) {
        this.dataMap[key] = value as Any
    }

    // 判断键值对是否存在
    operator fun contains(key: Symbols.Symbol<*>): Boolean {
        return dataMap.containsKey(key)
    }

    // 合并两个字典
    operator fun plus(other: Dict): Dict {
        val map = HashMap<Symbols.Symbol<*>, Any>()
        map.putAll(dataMap)
        map.putAll(other.dataMap)
        return Dict(map)
    }

    // 剔除掉两个字典的共同部分
    operator fun minus(other: Dict): Dict {
        val map = HashMap<Symbols.Symbol<*>, Any>()
        map.putAll(dataMap)
        for (key in other.dataMap.keys) {
            map.remove(key)
        }
        return Dict(map)
    }

    // 合并另一个字典至本对象
    operator fun plusAssign(other: Dict) {
        dataMap.putAll(other.dataMap)
    }

    // 从本对象中剔除掉与另一个字典共同的部分
    operator fun minusAssign(other: Dict) {
        for (key in other.dataMap.keys) {
            dataMap.remove(key)
        }
    }

    // 判断两个字典的内容是否相等
    override operator fun equals(other: Any?): Boolean {
        if (other !is Dict) return false
        if (dataMap.hashCode() == other.hashCode()) return true
        if (dataMap.size != other.dataMap.size) return false
        for (entry in dataMap.entries) {
            val otherVal = other.dataMap[entry.key]
            if (otherVal != entry.value) return false
        }
        return true
    }
}
