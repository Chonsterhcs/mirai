/*
 * Copyright 2019-2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AFFERO GENERAL PUBLIC LICENSE version 3 license that can be found via the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:JvmMultifileClass
@file:JvmName("ConsoleUtils")

package net.mamoe.mirai.console.plugin.jvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import net.mamoe.mirai.console.internal.util.JavaPluginSchedulerImpl
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext


/**
 * 拥有生命周期管理的简单 Java 线程池.
 *
 * 在插件被 [卸载][JavaPlugin.onDisable] 时将会自动停止.
 *
 * @see JavaPlugin.scheduler 获取实例
 */
public interface JavaPluginScheduler : CoroutineScope {
    /**
     * 新增一个 Repeating Task (定时任务)
     *
     * 这个 Runnable 会被每 [intervalMs] 调用一次(不包含 [runnable] 执行时间)
     *
     * @see Future.cancel 取消这个任务
     */
    public fun repeating(intervalMs: Long, runnable: Runnable): Future<Void?>

    /**
     * 新增一个 Delayed Task (延迟任务)
     *
     * 在延迟 [delayMillis] 后执行 [runnable]
     */
    public fun delayed(delayMillis: Long, runnable: Runnable): CompletableFuture<Void?>

    /**
     * 新增一个 Delayed Task (延迟任务)
     *
     * 在延迟 [delayMillis] 后执行 [runnable]
     */
    public fun <R> delayed(delayMillis: Long, runnable: Callable<R>): CompletableFuture<Void?>

    /**
     * 异步执行一个任务, 最终返回 [Future], 与 Java 使用方法无异, 但效率更高且可以在插件关闭时停止
     */
    public fun <R> async(supplier: Callable<R>): Future<R>

    /**
     * 异步执行一个任务, 没有返回
     */
    public fun async(runnable: Runnable): Future<Void?>

    public companion object {
        /**
         * 创建一个 [JavaPluginScheduler]
         */
        @JvmStatic
        @JvmName("create")
        public operator fun invoke(parentCoroutineContext: CoroutineContext): JavaPluginScheduler =
            JavaPluginSchedulerImpl(parentCoroutineContext)
    }
}