package com.tutedude.ecommerce.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tutedude.ecommerce.domain.models.Product
import com.tutedude.ecommerce.ui.auth.LoginScreen
import com.tutedude.ecommerce.ui.auth.RegisterScreen
import com.tutedude.ecommerce.ui.favorites.FavoritesScreen
import com.tutedude.ecommerce.ui.home.HomeScreen
import com.tutedude.ecommerce.ui.product.ProductDetailsScreen
import com.tutedude.ecommerce.ui.upload.UploadScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val productAdapter = moshi.adapter(Product::class.java)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToProductDetails = { product ->
                    val json = Uri.encode(productAdapter.toJson(product))
                    navController.navigate("product_details/$json")
                },
                onNavigateToUpload = { navController.navigate("upload") },
                onNavigateToFavorites = { navController.navigate("favorites") }
            )
        }
        composable(
            route = "product_details/{productJson}",
            arguments = listOf(navArgument("productJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("productJson")
            val product = json?.let { productAdapter.fromJson(Uri.decode(it)) }
            
            if (product != null) {
                ProductDetailsScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
        composable("upload") {
            UploadScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                onNavigateToProductDetails = { product ->
                    val json = Uri.encode(productAdapter.toJson(product))
                    navController.navigate("product_details/$json")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
