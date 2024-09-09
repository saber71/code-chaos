package heraclius.core.dict

import java.util.WeakHashMap

// 字典键值对
class Dict(private val dataMap: MutableMap<Symbols.Symbol<*>, Any> = mutableMapOf()) {
    // 父级字典
    private val parents = mutableSetOf<Dict>()

    // 删除的键值对
    private var removedKey = WeakHashMap<Symbols.Symbol<*>, Any>()

    // 继承父级字典
    fun extend(parent: Dict): Dict {
        parents.add(parent)
        return this
    }

    // 删除键值对
    fun remove(key: Symbols.Symbol<*>): Dict {
        removedKey.put(key, true)
        dataMap.remove(key)
        return this
    }

    // 获取键值对，未找到则返回null
    fun <V> valueOrNull(key: Symbols.Symbol<V>): V? {
        if (removedKey.containsKey(key)) return null
        if (!dataMap.containsKey(key)) {
            for (parent in parents.reversed()) {
                val value = parent.valueOrNull(key)
                if (value != null) return value
            }
            return null
        } else {
            @Suppress("UNCHECKED_CAST")
            return this.dataMap[key] as V
        }
    }

    // 获取键值对，未找到则返回默认值
    fun <V> value(key: Symbols.Symbol<V>, default: V): V {
        val value = valueOrNull(key)
        if (value == null) return default
        return value
    }

    // 复制本对象
    fun clone(): Dict {
        val result = Dict(dataMap.toMap().toMutableMap())
        result.parents.addAll(parents)
        result.removedKey.putAll(removedKey)
        return result
    }

    // 将Dict的所有键值对转换为Map
    fun toMap(): Map<Symbols.Symbol<*>, Any> {
        val result = mutableMapOf<Symbols.Symbol<*>, Any>()
        for (parent in parents) {
            result.putAll(parent.dataMap)
        }
        result.putAll(dataMap)
        for (key in removedKey.keys) {
            result.remove(key)
        }
        return result
    }

    // 使用hashMap作为本对象的hashcode
    override fun hashCode(): Int {
        return dataMap.hashCode()
    }

    // 使用hashMap的toString
    override fun toString(): String {
        return toMap().toString()
    }

    // 获取键值对
    operator fun <V> get(key: Symbols.Symbol<V>): V {
        return this.valueOrNull(key) ?: throw RuntimeException("$key not found")
    }

    // 设置键值对
    operator fun <V> set(key: Symbols.Symbol<V>, value: V) {
        this.dataMap[key] = value as Any
        removedKey.remove(key)
    }

    // 判断键值对是否存在
    operator fun contains(key: Symbols.Symbol<*>): Boolean {
        if (removedKey.containsKey(key)) return false
        if (!dataMap.containsKey(key)) {
            for (parent in parents.reversed()) {
                if (parent.contains(key)) return true
            }
            return false
        }
        return true
    }

    // 合并两个字典
    operator fun plus(other: Dict): Dict {
        val map = mutableMapOf<Symbols.Symbol<*>, Any>()
        map.putAll(toMap())
        map.putAll(other.toMap())
        return Dict(map)
    }

    // 剔除掉两个字典的共同部分
    operator fun minus(other: Dict): Dict {
        val result = clone()
        for (key in other.toMap().keys) {
            result.remove(key)
        }
        return result
    }

    // 合并另一个字典至本对象
    operator fun plusAssign(other: Dict) {
        dataMap.putAll(other.toMap())
    }

    // 从本对象中剔除掉与另一个字典共同的部分
    operator fun minusAssign(other: Dict) {
        for (key in other.dataMap.keys) {
            remove(key)
        }
    }

    // 判断两个字典的内容是否相等
    override operator fun equals(other: Any?): Boolean {
        if (other !is Dict) return false
        if (dataMap.hashCode() == other.hashCode()) return true
        val map1 = this.toMap()
        val map2 = other.toMap()
        if (map1.size != map2.size) return false
        for (entry in map1.entries) {
            val otherVal = map2[entry.key]
            if (otherVal != entry.value) return false
        }
        return true
    }
}
