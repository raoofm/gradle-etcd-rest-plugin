/*
 * Copyright 2014 the original author or authors.
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
package com.cdancy.gradle.etcd.rest.tasks.members

import com.cdancy.gradle.etcd.rest.tasks.AbstractEtcdRestTask;

/**
 * List all instance in cluster of <a href="https://github.com/cdancy/etcd-rest/blob/master/src/main/java/com/cdancy/etcd/rest/domain/members/Member.java">Member</a>
 */
class List extends AbstractEtcdRestTask {

    private def members = []

    @Override
    void runRemoteCommand(etcdClient) {
        def api = etcdClient.api().membersApi()
        members.addAll api.list()
        logger.quiet "Members: ${members.toString()}"
    }

    def members() { members }
}
