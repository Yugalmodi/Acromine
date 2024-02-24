package com.yugal.acrominefinder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yugal.acrominefinder.model.AcromineDataItem
import com.yugal.acrominefinder.model.Lf
import com.yugal.acrominefinder.network.ApiResponse
import com.yugal.acrominefinder.ui.theme.AcromineFinderTheme
import com.yugal.acrominefinder.viewmodel.AcronymViewModel

lateinit var listViewModel:AcronymViewModel

@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    AcromineFinderTheme {
        MainView()
    }
}

@Composable
fun MainView(){
    listViewModel = hiltViewModel()
    val apiData by listViewModel.dataList.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ) {
        SearchAppBarNew()
        when(apiData){
            is ApiResponse.Loading ->{
                Loader()
            }
            is ApiResponse.Success ->{
                val response = (apiData as ApiResponse.Success).data
                response?.let {
                    MyCardsView(it)
                }
            }
            is ApiResponse.Failure ->{
                val errorMessage = (apiData as ApiResponse.Failure).errorMessage
                errorMessage?.let {
                    Text(text = errorMessage)
                }
            }
            else->{

            }
        }
    }

}

@Composable
fun Loader(){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier
            .size(50.dp),
            color = Color.White
        )
    }
}

@Composable
fun SearchAppBarNew() {
    Column {
        var searchText by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        val radioList = listOf("sf", "lf")
        var selectedOption by remember { mutableStateOf(radioList[0]) }

        TextField(
            value = searchText,
            onValueChange ={newText->searchText=newText},
            label = { Text(text = "Search")},
            leadingIcon = {
               Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if(searchText.isNotEmpty()) {
                    IconButton(onClick = {searchText=""}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Search Icon")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                listViewModel.onSearch(searchText=searchText.trim(), searchFor = selectedOption)
                focusManager.clearFocus()
            }),
            modifier = Modifier.fillMaxWidth()
        )

        Row{
            radioList.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .clickable {
                            selectedOption = item
                        }
                ) {
                    RadioButton(
                        selected = item == selectedOption,
                        onClick = {selectedOption = item}
                    )
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun MyCardsView(acronymData: List<AcromineDataItem>){
    LazyColumn(
        content = {
            items(acronymData[0].lfs) {
                MyCardItemView(it)
            }
        }
    )
}

@Composable
fun MyCardItemView(acronym:Lf){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                //onClick(acronym)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Yellow
        ),
    ) {
        Text(text = acronym.lf,
            color = Color.Blue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 5.dp)
        )
        Row{
            Text(text = "Frequency = ${acronym.freq}",
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp, end = 5.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Since = ${acronym.since}",
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp, end = 5.dp)
            )
        }
    }
}
