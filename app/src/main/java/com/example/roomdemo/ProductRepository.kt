package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductRepository(private val productDAO: ProductDAO) {

    val allProducts: LiveData<List<Product>> = productDAO.getAllProducts()
    val searchResults = MutableLiveData<List<Product>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newProduct: Product){
        coroutineScope.launch (Dispatchers.IO){
            productDAO.insertProduct(newProduct)
        }
    }

    fun deleteProduct(name: String){
        coroutineScope.launch (Dispatchers.IO){
            productDAO.deleteProduct(name)
        }
    }

    fun findProduct(name: String){
        coroutineScope.launch (Dispatchers.Main){
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Product>?> =
        coroutineScope.async (Dispatchers.IO){
            return@async productDAO.findProduct(name)
        }

}