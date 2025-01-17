/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.internal.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.*
import net.mamoe.mirai.utils.JavaFriendlyAPI
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.EmptyCoroutineContext

@JavaFriendlyAPI
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@net.mamoe.mirai.utils.EventListenerLikeJava
internal class SimpleListenerHostTestJava : AbstractEventTest() {
    @Test
    fun testJavaSimpleListenerHostWork() {
        val called = AtomicBoolean()
        val host: SimpleListenerHost = object : SimpleListenerHost() {
            @EventHandler
            @net.mamoe.mirai.utils.EventListenerLikeJava
            fun testListen(
                event: AbstractEvent?
            ) {
                println(event)
                called.set(true)
            }
        }
        val scope = CoroutineScope(EmptyCoroutineContext)
        scope.globalEventChannel().registerListenerHost(host)
        runBlocking { object : AbstractEvent() {}.broadcast() }
        if (!called.get()) {
            throw AssertionError("JavaTest: SimpleListenerHost Failed.")
        }
    }
}