package com.coders.chatapplication.commons.data.mapper

interface DomainMappable<T> {
	fun asDomain() : T
}