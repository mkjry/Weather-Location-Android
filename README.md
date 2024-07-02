Coding procedure (Retrofit + live data + Kotlin)

1. Dependency : Retrofit, LiveData and ViewModel
2. Data class : Data model for API response
3. response data type : Interface of Retrofit for return data type, name
4. Set Up Retrofit : Retrofit instance, initialize
5. Repository : extract data from API response
6. ViewModel class : LiveData<types>
		class vm : viewModel() {
			val user: LiveData<types>
		}
7. Observe on Activity : vm.v.observ(x, observer{
		v.let{
			binding.text = (it)
		}})
8. layout_.xml
