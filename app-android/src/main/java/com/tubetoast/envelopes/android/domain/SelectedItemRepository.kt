package com.tubetoast.envelopes.android.domain

import com.tubetoast.envelopes.android.presentation.models.SelectableItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class SelectedItemRepository<I : SelectableItem<*>>(
    initial: suspend () -> Set<I>
) {
    private val scope = CoroutineScope(Job())
    private val _items: MutableStateFlow<Set<I>> = MutableStateFlow(emptySet())

    val items: StateFlow<Set<I>> get() = _items.asStateFlow()

    fun changeSelection(change: Set<I>.() -> Set<I>) {
        _items.update {
            it.change()
        }
    }

    init {
        scope.launch {
            _items.value = initial()
        }
    }
}
