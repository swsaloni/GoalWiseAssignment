package com.demo.assignment.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playablo.school.myapplication.R
import com.demo.assignment.activity.SuccessActivity
import com.demo.assignment.data.FundDetails
import kotlinx.android.synthetic.main.adapter_fund_details.view.*

class FundsAdapter(context: Context, data: ArrayList<FundDetails>): RecyclerView.Adapter<FundsAdapter.ViewHolder>()
{
    val dataList = data
    val mContext = context

    private val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var fundname = itemView.tv_fundName
        var minsipamount = itemView.tv_min_sip_amount
        var minsipmultiple = itemView.tv_min_sip_multiple
        var sipdates = itemView.tv_sip_dates
        var addFund = itemView.et_funds
        var btnAddFund = itemView.btn_addFund
        var actionAdd = itemView.actionAdd
        var addFundLayout= itemView.tv_addFund

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View?
        view = inflater.inflate(R.layout.adapter_fund_details, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
       return dataList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.fundname.text = dataList[position].fundname
        viewHolder.minsipamount.text = dataList[position].minsipamount.toString()
        viewHolder.minsipmultiple.text = dataList[position].minsipmultiple.toString()
        val datesString = dataList[position].sipdates.joinToString(", ")+" of every month"
        viewHolder.sipdates.text= datesString
        viewHolder.addFundLayout.visibility= View.GONE
        viewHolder.addFund.visibility= View.GONE
        viewHolder.btnAddFund.visibility= View.GONE
        viewHolder.actionAdd.setOnClickListener { v->
            viewHolder.addFundLayout.visibility= View.VISIBLE
            viewHolder.addFund.visibility= View.VISIBLE
            viewHolder.btnAddFund.visibility= View.VISIBLE

        }

        viewHolder.btnAddFund.setOnClickListener { v->
            val fundsAmount = viewHolder.addFund.text.toString()
            val amt = fundsAmount.toInt()
            if(TextUtils.isEmpty(fundsAmount.toString()))
            {
                viewHolder.addFund.error="Enter a valid Amount"
            }
            else
            {
                if (amt > dataList[position].minsipamount && amt % dataList[position].minsipmultiple==0)
                {
                    val intent = Intent(mContext,
                        SuccessActivity::class.java)
                    intent.putExtra("fundName",dataList[position].fundname)
                    mContext.startActivity(intent)
                }
                else
                {

                }
            }
        }


    }

}