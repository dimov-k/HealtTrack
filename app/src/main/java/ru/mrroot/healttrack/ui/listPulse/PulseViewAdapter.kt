package ru.mrroot.healttrack.ui.listPulse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.mrroot.healttrack.data.firestore.Pulse
import ru.mrroot.healttrack.databinding.ItemFragmentPulseBinding
import java.text.SimpleDateFormat
import java.util.*

class PulseViewAdapter(
) : RecyclerView.Adapter<PulseViewAdapter.ViewHolder>() {

    private val pulseList = emptyList<Pulse>().toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemFragmentPulseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


    fun update(newPulseList: List<Pulse>) {
        val diffResult = DiffUtil.calculateDiff(PulseDiffUtilCallback(pulseList, newPulseList))
        pulseList.clear()
        pulseList.addAll(newPulseList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val createdFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // myView.setBackground(myGradBg)
        val upperPressure = pulseList[position].upperPressure
        val lowerPressure = pulseList[position].lowerPressure
        holder.item.setBackground(ColorPressureGradientDrawable().getGradient(upperPressure,lowerPressure))
        holder.time.text = createdFormat.format(pulseList[position].date)
        holder.upperPressure.text = upperPressure.toString()
        holder.lowerPressure.text = lowerPressure.toString()
        holder.pulse.text = pulseList[position].pulse.toString()
    }

    override fun getItemCount(): Int = pulseList.size

    inner class ViewHolder(binding: ItemFragmentPulseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val item = binding.itemList
        val time: TextView = binding.time
        val upperPressure: TextView = binding.upperPressure
        val lowerPressure: TextView = binding.lowerPressure
        val pulse: TextView = binding.pulse
    }

}