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
