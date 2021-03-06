/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.marcphilipp.gradle.nexus

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.container
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property

import java.time.Duration

@Suppress("UnstableApiUsage")
open class NexusPublishExtension(project: Project) {

    companion object {
        internal const val NAME = "nexusPublishing"
    }

    val useStaging: Property<Boolean> = project.objects.property<Boolean>().apply {
        set(project.provider { !project.version.toString().endsWith("-SNAPSHOT") })
    }

    val packageGroup: Property<String> = project.objects.property<String>().apply {
        set(project.provider { project.group.toString() })
    }

    val clientTimeout: Property<Duration> = project.objects.property<Duration>().apply {
        set(Duration.ofMinutes(1))
    }

    val connectTimeout: Property<Duration> = project.objects.property<Duration>().apply {
        set(Duration.ofMinutes(1))
    }

    val repositories: NexusRepositoryContainer = DefaultNexusRepositoryContainer(project.container(NexusRepository::class) { name ->
        project.objects.newInstance(NexusRepository::class, name, project)
    })

    fun repositories(action: Action<in NexusRepositoryContainer>) = action.execute(repositories)
}
