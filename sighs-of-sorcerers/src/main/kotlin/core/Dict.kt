package heraclius.core

import core.Utils

class Dict {
    private val dataMap: MutableMap<Any, Any> = HashMap()

    private val keyClsMapValues: MutableMap<Class<*>, MutableList<Any>> = HashMap()

    // 清除所有数据
    fun clear() {
        dataMap.clear()
        keyClsMapValues.clear()
    }

    /**
     * 根据给定的键和值设置数据
     * 如果键已存在，则更新其值，并从旧值列表中移除旧值
     *
     * @param key 要设置或更新的 DataStoreKey 对象
     * @param v   与键关联的值
     * @param <V> 值的类型
     * @return 设置的值
    </V> */
    fun <V> set(key: Any, v: V, ignoreSuperClasses: Boolean = false): V {
        val oldValue = dataMap.put(key, v as Any)
        if (key is Class<*>) return v
        if (ignoreSuperClasses) return v
        val superClasses = Utils.getParentClasses(key)
        for (superClass in superClasses) {
            val list = keyClsMapValues.computeIfAbsent(superClass) { _ -> ArrayList() }
            if (oldValue != null) {
                list.remove(oldValue)
            }
            list.add(v)
        }
        return v
    }

    /**
     * 使用字符串键获取数据，内部会转换为 DataStoreKey 对象
     *
     * @param key 任意类型的键
     * @param <V> 期望返回的值的类型
     * @return 与键关联的值，如果键不存在则返回 null
     */
    fun <V> get(key: Any): V? {
        @Suppress("UNCHECKED_CAST")
        return this.dataMap[key] as V?
    }

    /**
     * 根据键获取数据存储中的值
     *
     * @param key 任意形式的键
     * @param <V> 泛型参数，表示返回的值的类型
     * @return 与键相关联的值
    </V> */
    fun <V> fetch(key: Any): V {
        val result = this.get<V>(key) ?: throw RuntimeException("DataStoreKey not found")
        return result
    }

    /**
     * 获取与指定 DataStoreKey 类型关联的所有值列表
     *
     * @param keyClass DataStoreKey 的类类型
     * @return 与指定键类型关联的值列表，如果类型不存在则返回空列表
    </V> */
    fun <V> getAll(keyClass: Class<*>): List<V> {
        val list = keyClsMapValues[keyClass] ?: return emptyList()
        @Suppress("UNCHECKED_CAST")
        return list.toList() as List<V>
    }

    /**
     * 使用字符串键检查数据是否存在，内部会转换为 DataStoreKey 对象
     *
     * @param key 任意类型的键
     * @return 如果键存在则返回 true，否则返回 false
     */
    fun has(key: Any): Boolean {
        return dataMap.containsKey(key)
    }

    fun remove(key: Any) {
        if (!dataMap.containsKey(key)) return
        val value = dataMap.remove(key)
        for (superClass in Utils.getParentClasses(key)) {
            val list = keyClsMapValues[superClass] ?: continue
            list.remove(value)
        }
    }
}
