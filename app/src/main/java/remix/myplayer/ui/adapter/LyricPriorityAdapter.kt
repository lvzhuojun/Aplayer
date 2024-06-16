package remix.myplayer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import remix.myplayer.R
import remix.myplayer.bean.misc.LyricPriority
import remix.myplayer.ui.adapter.holder.BaseViewHolder
import remix.myplayer.util.SPUtil


class LyricPriorityAdapter(context: Context?, layoutId: Int) : BaseAdapter<LyricPriority, LyricPriorityAdapter.LyricPriorityHolder>(layoutId) {

  init {
    val temp: ArrayList<LyricPriority> = Gson().fromJson(SPUtil.getValue(context, SPUtil.LYRIC_KEY.NAME, SPUtil.LYRIC_KEY.PRIORITY_LYRIC, SPUtil.LYRIC_KEY.DEFAULT_PRIORITY),
        object : TypeToken<List<LyricPriority>>() {}.type)

    val all = listOf(
        LyricPriority.EMBEDDED,
        LyricPriority.LOCAL,
        LyricPriority.KUGOU,
        LyricPriority.NETEASE,
        LyricPriority.QQ,
        LyricPriority.IGNORE)
    if (temp.size < all.size) {
      if (!temp.contains(LyricPriority.QQ)) {
        temp.add(2,LyricPriority.QQ)
      }
      if (!temp.contains(LyricPriority.IGNORE)) {
        temp.add(temp.size, LyricPriority.IGNORE)
      }

    }

    setDataList(temp)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricPriorityHolder {
    return LyricPriorityHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
  }

  override fun convert(holder: LyricPriorityHolder, lyricPriority: LyricPriority?, position: Int) {
    if(lyricPriority == null){
      return
    }

    holder.view.findViewById<TextView>(R.id.item_title)?.text = lyricPriority.desc
    holder.view.setOnClickListener {

    }
  }

  class LyricPriorityHolder(val view: View) : BaseViewHolder(view)
}
