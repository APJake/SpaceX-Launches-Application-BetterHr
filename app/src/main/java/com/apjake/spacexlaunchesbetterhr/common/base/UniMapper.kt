package com.apjake.spacexlaunchesbetterhr.common.base

interface UniMapper<A,B> {
    fun map(data: A): B
}