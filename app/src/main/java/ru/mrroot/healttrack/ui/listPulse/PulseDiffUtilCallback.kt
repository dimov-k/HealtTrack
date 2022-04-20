package ru.mrroot.healttrack.ui.listPulse

import androidx.recyclerview.widget.DiffUtil
import ru.mrroot.healttrack.data.firestore.Pulse

class PulseDiffUtilCallback(
    private val oldList: List<Pulse>,
    private val newList: List<Pulse>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}