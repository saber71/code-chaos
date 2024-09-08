package heraclius.core.dict

import java.lang.ref.WeakReference
import java.util.*

/**
 * 符号管理对象，用于创建和管理符号与其描述之间的映射。
 */
object Symbols {
    // 使用WeakHashMap存储符号和描述的映射关系，允许GC回收不再使用的符号。
    private val symbolMapDescription = WeakHashMap<Symbol<*>, String?>()

    // 使用WeakHashMap存储描述和符号的映射关系，允许GC回收不再使用的描述。
    private val descriptionMapSymbol = WeakHashMap<String?, WeakReference<Symbol<*>>>()

    /**
     * 创建或获取一个符号，并将其与描述关联。
     *
     * @param description 符号的描述，可以为空。
     * @param symbol 如果提供，则使用此符号，否则创建一个新的符号。
     * @return 与描述关联的符号。
     * @throws RuntimeException 如果尝试重复使用相同的描述。
     */
    fun <V> of(description: String? = null, symbol: Symbol<V>? = null): Symbol<V> {
        var symbol1 = symbol
        if (symbol1 == null) symbol1 = Symbol { symbolMapDescription[symbol1] }

        // 确保描述唯一性
        val oldSymbol = from<V>(description)
        if (oldSymbol != null) throw RuntimeException("description has repeated")

        // 将描述与符号关联
        symbolMapDescription[symbol1] = description
        // 将符号与描述关联
        if (!description.isNullOrEmpty()) descriptionMapSymbol[description] = WeakReference(symbol1)
        return symbol1
    }

    /**
     * 根据描述获取符号。
     *
     * @param description 要查找的符号的描述。
     * @return 匹配的符号及其描述的Map.Entry，如果未找到则返回null。
     */
    fun <V> from(description: String?): Symbol<V>? {
        @Suppress("UNCHECKED_CAST")
        return descriptionMapSymbol[description]?.get() as Symbol<V>?
    }

    /**
     * 符号类，持有符号的描述信息和对应的值类型泛型。
     *
     * @property descriptor 一个lambda表达式，用于延迟获取符号的描述。
     */
    class Symbol<V>(private val descriptor: (() -> String?)) {
        override fun toString(): String {
            // 调用descriptor获取描述，并处理可能的空值
            return "Symbol(${descriptor.invoke() ?: ""})"
        }
    }
}
