package com.example.cryptocurrencyapp.domain.use_case.get_coins

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    // override because we can call this class as a function @@
    // use Flow because it will return (emit) many value in a period of time
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success(coins))
        } catch (e: HttpException){ // when it return an error api
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException){   // when lost connect the internet
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}