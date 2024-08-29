package com.example.fragment.project

import androidx.lifecycle.viewModelScope
import com.example.fragment.project.data.HotKey
import com.example.fragment.project.data.HotKeyList
import com.example.fragment.project.data.Tree
import com.example.fragment.project.data.TreeList
import com.example.fragment.project.database.user.User
import com.example.fragment.project.utils.WanHelper
import com.example.miaow.base.http.get
import com.example.miaow.base.vm.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WanUiState(
    var hotKeyResult: List<HotKey> = ArrayList(),
    var treeResult: List<Tree> = ArrayList(),
    var searchHistoryResult: MutableList<String> = ArrayList(),
    var webBookmarkResult: MutableList<String> = ArrayList(),
    var webHistoryResult: MutableList<String> = ArrayList(),
    var user: User? = User("", "", ""),
    var updateTime: Long = 0
)

class WanViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(WanUiState())

    val uiState: StateFlow<WanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hotKeyList = async { getHotKeyList() }
            val treeList = async { getTreeList() }
            val searchHistoryList = async { WanHelper.getSearchHistory() }
            val webBookmarkList = async { WanHelper.getWebBookmark() }
            val webHistoryList = async { WanHelper.getWebHistory() }
            WanHelper.getUser().collect { user ->
                _uiState.update { state ->
                    hotKeyList.await().data?.let { data ->
                        state.hotKeyResult = data
                    }
                    treeList.await().data?.let { data ->
                        state.treeResult = data
                    }
                    searchHistoryList.await().let {
                        state.searchHistoryResult = it
                    }
                    webHistoryList.await().let {
                        state.webHistoryResult = it
                    }
                    webBookmarkList.await().let {
                        state.webBookmarkResult = it
                    }
                    state.copy(user = user, updateTime = System.nanoTime())
                }
            }
        }
    }

    /**
     * 获取搜索热词
     */
    private suspend fun getHotKeyList(): HotKeyList {
        return coroutineScope {
            get {
                setUrl("hotkey/json")
            }
        }
    }

    /**
     * 获取项目分类
     */
    private suspend fun getTreeList(): TreeList {
        return coroutineScope {
            get {
                setUrl("tree/json")
            }
        }
    }

    fun onSearchHistory(isAdd: Boolean, text: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                if (state.searchHistoryResult.contains(text)) {
                    state.searchHistoryResult.remove(text)
                }
                if (isAdd) {
                    state.searchHistoryResult.add(0, text)
                }
                state.copy(updateTime = System.nanoTime())
            }
            WanHelper.setSearchHistory(_uiState.value.searchHistoryResult)
        }
    }

    fun onWebBookmark(isAdd: Boolean, text: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                if (state.webBookmarkResult.contains(text)) {
                    state.webBookmarkResult.remove(text)
                }
                if (isAdd) {
                    state.webBookmarkResult.add(0, text)
                }
                state.copy(updateTime = System.nanoTime())
            }
            WanHelper.setWebBookmark(_uiState.value.webBookmarkResult)
        }
    }

    fun onWebHistory(isAdd: Boolean, text: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                if (state.webHistoryResult.contains(text)) {
                    state.webHistoryResult.remove(text)
                }
                if (isAdd) {
                    state.webHistoryResult.add(0, text)
                }
                state.copy(updateTime = System.nanoTime())
            }
            WanHelper.setWebHistory(_uiState.value.webHistoryResult)
        }
    }

}