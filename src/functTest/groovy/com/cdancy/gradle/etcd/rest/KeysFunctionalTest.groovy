package com.cdancy.gradle.etcd.rest

import org.gradle.testkit.runner.BuildResult
import spock.lang.Requires

@Requires({ TestPrecondition.ETCD_URL_REACHABLE })
class KeysFunctionalTest extends AbstractFunctionalTest {

    def "Can set and get key"() {

        buildFile << """
            task setKey(type: com.cdancy.gradle.etcd.rest.tasks.keys.SetKey) {
                key { "hello" }
                value { "world" }
            	doLast {
            		def foundInstance = instance()
            		println "Key action: " + foundInstance.action
            		println "Key node-key: " + foundInstance.node.key
                    println "Key node-value: " + foundInstance.node.value
            		println "Key ErrorMessage: " + foundInstance.errorMessage
            	}
            }

            task getKey(type: com.cdancy.gradle.etcd.rest.tasks.keys.GetKey, dependsOn: setKey) {
                key { "hello" }
                doLast {
                    def foundInstance = instance()
                    println "Key get action: " + foundInstance.action
                    println "Key get node-value: " + foundInstance.node.value
                }
            }
            
            task workflow {
                dependsOn getKey
            }
        """

        when:
        BuildResult result = build('workflow')

        then:
        result.output.contains('Get Key:')
        result.output.contains('Key action: set')
        result.output.contains('Key node-key: /hello')
        result.output.contains('Key node-value: world')
        result.output.contains('Key ErrorMessage: null')
        result.output.contains('Key get action: get')
        result.output.contains('Key get node-value: world')
    }

    def "Can set and get in-order key"() {

        buildFile << """
            task setKey(type: com.cdancy.gradle.etcd.rest.tasks.keys.SetKey) {
                key { "hello-in-order" }
                value { "world-in-order" }
                inOrder = true
            	doLast {
            		def foundInstance = instance()
            		println "Key action: " + foundInstance.action
            		println "Key node-key: " + foundInstance.node.key
                    println "Key node-value: " + foundInstance.node.value
            		println "Key ErrorMessage: " + foundInstance.errorMessage
            	}
            }

            task getKey(type: com.cdancy.gradle.etcd.rest.tasks.keys.GetKey, dependsOn: setKey) {
                key { "hello" }
                doLast {
                    def foundInstance = instance()
                    println "Key get action: " + foundInstance.action
                    println "Key get node-value: " + foundInstance.node.value
                }
            }

            task workflow {
                dependsOn getKey
            }
        """

        when:
        BuildResult result = build('workflow')

        then:
        result.output.contains('Set Key:')
        result.output.contains('Key action: create')
        result.output.contains('Key node-key: /hello-in-order')
        result.output.contains('Key node-value: world-in-order')
        result.output.contains('Key ErrorMessage: null')
        result.output.contains('Key get action: get')
        result.output.contains('Key get node-value: world')
    }
}
