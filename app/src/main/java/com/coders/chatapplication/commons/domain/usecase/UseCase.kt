package com.coders.chatapplication.commons.domain.usecase

import com.coders.chatapplication.commons.domain.exception.Failure
import com.coders.chatapplication.commons.domain.response.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A UseCase executes a business case.
 * @param <Params> parameter class which is consumed by the UseCase
 * @param <Type> return type which will be returned by the UseCase
 */
abstract class UseCase<in Params, out Type> where Type : Any {

	/**
	 * Executes the use case.
	 * @param params <Params> optional param for this use case execution.
	 * @return <R> the type of result.
	 */
	abstract suspend fun execute(params: Params): Either<Failure, Type>

	open operator fun invoke(
		scope: CoroutineScope,
		params: Params,
		onResult: (Either<Failure, Type>) -> Unit = {}
	) {
		val backgroundJob = scope.async { execute(params) }
		scope.launch { onResult(backgroundJob.await()) }
	}
}