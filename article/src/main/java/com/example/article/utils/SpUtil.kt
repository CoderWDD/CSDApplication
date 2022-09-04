package com.example.article.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`



object SpUtil {

    const val PREFERENCE_NAME = "setting"

    private val gson = Gson()
    private const val MODE = Context.MODE_PRIVATE

    /**
     * 获得指定 SharedPreference
     * @param context activity-context
     * */
    private fun getSp(context: Context)
            = context.getSharedPreferences(PREFERENCE_NAME, MODE)
    /**
     * 获得指定 SharedPreference 的编辑器
     * @param context activity-context
     * */
    private fun getSpEditor(context: Context)
            = context.getSharedPreferences(PREFERENCE_NAME, MODE).edit()
    /**
     * 清空数据
     * @param context activity-context
     * */
    fun clearSp(context: Context){
        getSpEditor(context).clear().apply()
    }

    /**
     *  写入String字段
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的String字段
     * */
    fun putString(context: Context, key: String, value: String){
        getSpEditor(context).putString(key, value).apply()
    }

    /**
     * 获取 String 字段(默认值为"")
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回""
     * @see getString
     */
    fun getString(context: Context, key: String): String
            = getString(context, key, "")

    /**
     * 获取 String 字段
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回""
     */
    fun getString(context: Context, key: String, defaultValue: String): String
            = getSp(context).getString(key, defaultValue)?: ""

    /**
     *  写入Int字段
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 Int字段
     * */
    fun putInt(context: Context, key: String, value: Int){
        getSpEditor(context).putInt(key, value).apply()
    }

    /**
     * 获取 Int 字段(默认值为-1)
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1
     * @see getInt
     */
    fun getInt(context: Context, key: String): Int
            = getInt(context, key, -1)

    /**
     * 获取 Int 字段
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1
     */
    fun getInt(context: Context, key: String, defaultValue: Int): Int
            = getSp(context).getInt(key, defaultValue)

    /**
     *  写入Long字段
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 Long字段
     * */
    fun putLong(context: Context, key: String, value: Long){
        getSpEditor(context).putLong(key, value).apply()
    }

    /**
     * 获取 Long 字段(默认值为-1)
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1
     * @see getLong
     */
    fun getLong(context: Context, key: String): Long
            = getLong(context, key, -1)

    /**
     * 获取 Long 字段
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1
     */
    fun getLong(context: Context, key: String, defaultValue: Long): Long
            = getSp(context).getLong(key, defaultValue)

    /**
     *  写入Float字段
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 Float字段
     * */
    fun putFloat(context: Context, key: String, value: Float){
        getSpEditor(context).putFloat(key, value).apply()
    }

    /**
     * 获取 Float 字段(默认值为-1f)
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1f
     * @see getFloat
     */
    fun getFloat(context: Context, key: String): Float
            = getFloat(context, key, -1f)

    /**
     * 获取 Float 字段
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回-1f
     */
    fun getFloat(context: Context, key: String, defaultValue: Float): Float
            = getSp(context).getFloat(key, defaultValue)

    /**
     *  写入Boolean字段
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 Boolean字段
     * */
    fun putBoolean(context: Context, key: String, value: Boolean){
        getSpEditor(context).putBoolean(key, value).apply()
    }

    /**
     * 获取 Boolean 字段(默认值为false)
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回false
     * @see getBoolean
     */
    fun getBoolean(context: Context, key: String): Boolean
            = getBoolean(context, key, false)

    /**
     * 获取 Boolean 字段
     * @param context activity-context
     * @param key 键值
     * @return key对应的value,不存在则返回false
     */
    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean
            = getSp(context).getBoolean(key, defaultValue)



    fun putBooleanList(context: Context, key: String, value: List<Boolean>){
        val editor = getSpEditor(context)
        editor.putInt(key, value.size)
        for(pos in value.indices) editor.putBoolean(key+pos, value[pos])
        editor.apply()
    }

    fun getBooleanList(context: Context, key: String): List<Boolean>{
        val size = getInt(context, key)
        val value = mutableListOf<Boolean>()
        return value.apply {
            for (i in 0 until size) {
                value.add(getBoolean(context, key + i, false));
            }
        }
    }

    /**
     *  写入 List<String>
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 List
     * */
    fun putStringList(context: Context, key: String, value: List<String>){
        val editor = getSpEditor(context)
        editor.putInt(key, value.size)
        for (pos in value.indices)  editor.putString(key + pos, value[pos])
        editor.apply()
    }

    /**
     * 获取 List<String> (默认值为 空List)
     *   @param context activity-context
     *   @param key 键值
     *   @return key 对应的 List， 无则返回 空的List
     * */
    fun getStringList(context: Context, key: String): List<String>{
        val size = getInt(context, key)
        val value = mutableListOf<String>()
        return value.apply {
            for (i in 0 until size) {
                value.add(getString(context, key + i, ""));
            }
        }
    }
    /**
     *  写入 List<Int>
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 List
     * */
    fun putIntList(context: Context, key: String, value: List<Int>){
        val editor = getSpEditor(context)
        editor.putInt(key, value.size)
        for (pos in 0 until value.size)  editor.putInt(key + pos, value[pos])
        editor.apply()
    }

    /**
     * 获取 List<Int> (默认值为 空List)
     *   @param context activity-context
     *   @param key 键值
     *   @return key 对应的 List， 无则返回 空的List
     * */
    fun getIntList(context: Context, key: String): List<Int>{
        val size = getInt(context, key)
        val value = mutableListOf<Int>()
        return value.apply {
            for (i in 0 until size) {
                value.add(getInt(context, key + i, -1));
            }
        }
    }
    /**
     *  写入 List<Long>
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 List
     * */
    fun putLongList(context: Context, key: String, value: List<Long>){
        val editor = getSpEditor(context)
        editor.putInt(key, value.size)
        for (pos in value.indices)  editor.putLong(key + pos, value[pos])
        editor.apply()
    }

    /**
     * 获取 List<Long> (默认值为 空List)
     *   @param context activity-context
     *   @param key 键值
     *   @return key 对应的 List， 无则返回 空的List
     * */
    fun getLongList(context: Context, key: String): List<Long>{
        val size = getInt(context, key)
        val value = mutableListOf<Long>()
        return value.apply {
            for (i in 0 until size) {
                value.add(getLong(context, key + i, -1));
            }
        }
    }
    /**
     *  写入 List<object>
     *  @param context activity-context
     *  @param key 键值
     *  @param value 写入的 List
     * */
    fun <T> putObjectList(context: Context, key: String, value: List<T>){
        val editor = getSpEditor(context)
        editor.putString(key, gson.toJson(value))
        editor.apply()
    }

    /**
     * 获取 List<Long> (默认值为 空List)
     *   @param context activity-context
     *   @param key 键值
     *   @return key 对应的 List， 无则返回 空的List
     * */
    fun <T> getObjectList(context: Context, key: String, clazz: Class<T>): List<T>{
        val value = mutableListOf<T>()
        val json = getString(context, key,"")
        if(json.isEmpty()) return value
        return value.apply {
            val listType = `$Gson$Types`.newParameterizedTypeWithOwner(
                null,
                List::class.java,  clazz
            )
            addAll(gson.fromJson(json, listType))
        }
    }

    /**
     * 写入 任意实体object
     * @param context activity-context
     * @param key 键值
     * @param any 任意实体
     * */
    fun <T>putObject(context: Context, key: String, any: T){
        val str = gson.toJson(any)
        getSpEditor(context).putString(key,str).apply()
    }

    /**
     * 获取 实体object
     *  @param context activity-context
     *  @param key 键值
     *  @param clazz 实体类
     *  @return object 返回实体类（无则null）
     * */
    fun <T> getObject(context: Context, key: String, clazz: Class<T>): T?{
        val json = getString(context, key, "")
        if(json.isEmpty()) return null
        return gson.fromJson(json, clazz)
    }
}