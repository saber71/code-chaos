package core

object Utils {
    // 缓存获取父类列表的函数
    private val parentClassesCache = mutableMapOf<Class<*>, List<Class<*>>>()

    /**
     * 获取对象的类信息
     *
     * @param obj 任意对象
     * @return 对象的类信息如果对象本身就是Class类型的实例，则直接返回该对象；否则返回对象的类信息
     *
     * 此函数的目的是为了以统一的方式获取对象的类信息，避免直接操作java内置方法可能带来的不便或限制
     */
    fun getClass(obj: Any): Class<*> {
        if (obj is Class<*>) return obj
        return obj.javaClass
    }

    /**
     * 获取指定类及其所有父类的列表
     *
     * @param obj 初始类对象，用于获取其父类信息
     * @return 返回包含指定类及其所有父类的列表
     *
     * 此函数旨在构建一个类的继承体系列表，从指定的类开始，逐级向上获取其父类，直至Any基类
     * 这在反射操作中尤为有用，可以帮助快速了解和操作类的继承关系
     */
    fun getParentClasses(obj: Any): List<Class<*>> {
        var cls = getClass(obj)
        val originClass = cls
        if (parentClassesCache.containsKey(cls)) return parentClassesCache[cls]!!
        val result = ArrayList<Class<*>>()
        result.add(cls)
        while (true) {
            val parentClass = cls.superclass
            if (parentClass == Any::class.java) break
            result.add(parentClass)
            cls = parentClass
        }
        val list = result.toList()
        parentClassesCache[originClass] = list
        return list
    }

    /**
     * 检查指定的类是否是另一个类的子类。
     *
     * 该函数通过检查`clazz`参数是否是`parentCls`参数的子类来实现。
     * 它通过遍历类的继承链直到`Any`类（Kotlin中所有类的超类，对应Java的`Object`类）来完成这个检查过程。
     *
     * @param clazz 要检查的类。
     * @param parentCls 要检查的潜在父类。
     * @return 如果`clazz`是`parentCls`的子类，则返回`true`；否则返回`false`。
     */
    fun instanceof(clazz: Class<*>, parentCls: Class<*>): Boolean {
        var cls = clazz
        while (true) {
            val parentClass = cls.superclass
            if (parentClass == Any::class.java) break
            if (parentClass == parentCls) return true
            cls = parentClass
        }
        return false
    }


    /**
     * 读取指定类的所有属性，并返回一个列表，其中包含这些属性的值。
     *
     * @param obj 要读取属性的对象
     * @param clazz 要读取属性的类
     * @return 包含属性值的列表
     */
    fun <T> readProperties(obj: Any, clazz: Class<T>): List<T> {
        val cls = obj.javaClass
        val properties = cls.declaredFields
        val result = ArrayList<T>()

        for (property in properties) {
            property.isAccessible = true
            if (clazz.isAssignableFrom(property.type)) {
                val value = property.get(obj)
                @Suppress("UNCHECKED_CAST")
                result.add(value as T)
            }
        }
        return result.toList()
    }

    /**
     * 获取类上的指定注解实例
     *
     * 此函数的目的是从给定的类上检索特定类型的注解实例它通过反射机制遍历类上所有的注解，
     * 并找到与请求注解类型匹配的第一个注解实例如果找不到匹配的注解，则返回null
     *
     * @param clazz 要检索注解的类可以是任何类型的类，但必须带有注解
     * @param annotationClass 要检索的特定注解类型的类例如，要检索`Target`注解，可以传入`Target::class.java`
     * @return 返回与请求注解类型匹配的注解实例，如果找不到则返回null
     */
    fun <A : Annotation> getAnnotationInstance(clazz: Class<*>, annotationClass: Class<A>): A? {
        @Suppress("UNCHECKED_CAST")
        return clazz.annotations.find { it.annotationClass.java == annotationClass } as? A
    }

    /**
     * 合并并去重多个列表
     *
     * @param list 需要合并的列表参数，使用vararg关键字使得可以接收任意数量的列表参数
     * @return 返回合并并去重后的列表
     *
     * 该函数的主要目的是将多个列表合并为一个列表，并去除重复的元素
     * 如果输入的列表参数为空，则直接返回一个空列表
     */
    fun <V> mergeAndDistinct(vararg list: List<V>): List<V> {
        // 检查列表是否为空，如果为空则直接返回一个空列表
        if (list.isEmpty()) return emptyList()

        // 初始化一个可变列表，用于存储合并后的结果
        val merged = list[0].toMutableList()

        // 遍历除第一个列表外的其他列表，将它们的元素添加到merged列表中
        for (ls in list.slice(1..list.lastIndex)) {
            merged += ls
        }

        // 返回合并后的列表，并去除重复的元素
        return merged.distinct()
    }
}
