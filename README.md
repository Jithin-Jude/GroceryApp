## [GroceryApp: A simple e-commerce app for groceries](https://drive.google.com/file/d/1DzXIe6ACeSb62D3gzsVOdjohm0F7UeWb/view?usp=sharing)
A simple grocery/e-commerce demo application that allows customers to log in using Google or a phone number. After onboarding, the app displays dishes across multiple categories. Customers can add dishes to the cart directly from the home screen and purchase the selected items with the desired quantities from the cart.

The application is developed using the MVVM architecture and utilizes Hilt and Dagger for dependency injection.

Notes
--
### Major components of appication:
1. <b>View layer:</b> Responsive UI is developed with the help of jetpack compose libraries such as `navigation-compose`, `compose.material3` & 3rd party libs like coil & country-picker. Google SignIn is achived using Google Credential Manager SDK.

2. <b>Data layer:</b> Data layer of this application is developed by leveraging `OkHttp`, `Retrofit`, `Dagger-Hilt`.

3. <b>State Management:</b> State across app is managed using `OnboardingViewModel` for authentication and customer data handling, while `ProductViewModel` serves as the single source of truth for both the Home and Cart screens.

### Things implemented to reduce network traffic & improve app performance:
1. <b>OkHttp caching:</b> Once the menu data is fetched for the first time, app will be able to show data in case of no or poor network conditions. This also helps to reduce network traffic since it getting the cached data. The data will cache for 7 days & this can be adjusted.

2. <b>Caching of image with Coil library & use of placeholders:</b> Use of placeholders enhance UI on loading images for the first time while caching ensure faster image loading for subsequent app sessions.

### Challenges faced during development & solutions arrived:
- During development, I faced a challenge where API calls using Retrofit and Kotlin suspend functions worked fine in debug mode but failed in release mode. The issue was caused by R8 removing Kotlin metadata and generic type information that Retrofit needs to understand the API response type at runtime. Because of this, the app was unable to fetch products from API.
I fixed the issue by adding the correct ProGuard rules to keep Retrofit classes, Kotlin metadata, and coroutine continuation information.

- App was not performing well in case of poor network (slow loading & data loss). Solved this issue by enabling OkHttp caching.
### App Demo: [download APK here](https://drive.google.com/file/d/1DzXIe6ACeSb62D3gzsVOdjohm0F7UeWb/view?usp=sharing)
![](https://github.com/Jithin-Jude/GroceryApp/blob/main/demo_images/gorcery_app_demo_jithin_k_jose.gif)

### Important Code snippets:
Compose Navigation:
```kt
NavHost(navController, startDestination = startDestination) {
            composable(Routes.LoginScreen.route) {
                LoginScreenView(
                    navController,
                    onboardingViewModel,
                )
            }
            composable(Routes.AskPhoneNumberScreen.route) {
                AskPhoneNumberScreenView(
                    navController,
                    onboardingViewModel,
                )
            }
            composable(Routes.VerifyOtpScreen.route) {
                VerifyOTPScreen(
                    navController,
                    onboardingViewModel,
                )
            }
            composable(Routes.AskNameScreen.route) {
                AskNameScreenView(
                    navController,
                    onboardingViewModel,
                )
            }
            composable(Routes.AskProfilePictureScreen.route) {
                AskProfilePictureScreenView(
                    navController,
                    onboardingViewModel,
                )
            }
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController = navController,
                    productViewModel = productViewModel,
                    onboardingViewModel = onboardingViewModel,
                    customerDataViewModel = customerDataViewModel,
                )
            }
            composable(Routes.CartScreen.route) {
                CartScreenView(
                    navController,
                    productViewModel,
                )
            }
        }
```

Fetch Menu:
```kt
    fun fetchAllProductsAndCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _productsLoader.postValue(true)
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        GroceryAppUtils.printLog("fetchAllProducts Loading")
                    }

                    is DataState.Success -> {
                        GroceryAppUtils.printLog("fetchAllProducts Success: ${result.data}")
                        _listOfProducts.postValue(
                            result.data.categories
                        )
                    }

                    is DataState.Error -> {
                        GroceryAppUtils.printLog("fetchAllProducts Error ${result.exception.message}")
                        _listOfProducts.postValue(
                            emptyList()
                        )
                    }
                }
            }
            _productsLoader.postValue(false)
        }
    }
```

Update Dish Count:
```kt
    private fun updateSelectedDishCount(
        dishId: Int,
        update: (Int) -> Int
    ) {
        val updatedCategories = _listOfProducts.value?.map { category ->
            category.copy(
                dishes = category.dishes.map { dish ->
                    if (dish.id == dishId) {
                        dish.copy(selectedCount = update(dish.selectedCount))
                    } else dish
                }
            )
        } ?: emptyList()

        _listOfProducts.postValue(updatedCategories)
    }
```

<i>Suggestions and improvements are welcome</i>

### 3rd party libraries used:
[Coil for image loading](https://coil-kt.github.io/coil/)

[Country Code Picker](https://github.com/arpitkatiyar1999/Country-Picker)

### References:

[Google Credential Manager](https://firebase.google.com/docs/auth/android/google-signin)

[State managemen in compose](https://developer.android.com/develop/ui/compose/state#managing-state)

[Forced OkHttp caching with retrofit](https://amitshekhar.me/blog/caching-with-okhttp-interceptor-and-retrofit#:~:text=can%20create%20a-,ForceCacheInterceptor,-in%20addition%20to)

[Image caching with Coil](https://medium.com/@kamal.lakhani56/coil-image-caching-jetpack-compose-354221918d70)
