package remix.myplayer.ui.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import remix.myplayer.R
import remix.myplayer.bean.mp3.Song
import remix.myplayer.databinding.ItemSearchReulstBinding
import remix.myplayer.misc.menu.SongPopupListener
import remix.myplayer.theme.Theme
import remix.myplayer.theme.ThemeStore.libraryBtnColor
import remix.myplayer.ui.activity.base.BaseActivity
import remix.myplayer.ui.adapter.SearchAdapter.SearchResHolder
import remix.myplayer.ui.adapter.holder.BaseViewHolder
import remix.myplayer.ui.misc.MultipleChoice

/**
 * Created by Remix on 2016/1/23.
 */
/**
 * 搜索结果的适配器
 */
class SearchAdapter(private val multiChoice: MultipleChoice<Song>, layoutId: Int) : BaseAdapter<Song, SearchResHolder>(layoutId) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResHolder {
    return SearchResHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
  }

  @SuppressLint("RestrictedApi")
  override fun convert(holder: SearchResHolder, song: Song?, position: Int) {
    if (song == null) {
      return
    }
    holder.binding.searchName.text = song.title
    holder.binding.searchDetail.text = String.format("%s-%s", song.artist, song.album)
    //封面
    Glide.with(holder.itemView)
        .load(song)
        .centerCrop()
        .placeholder(Theme.resolveDrawable(holder.itemView.context, R.attr.default_album))
        .error(Theme.resolveDrawable(holder.itemView.context, R.attr.default_album))
        .into(holder.binding.iv)

    //设置按钮着色
    val tintColor = libraryBtnColor
    Theme.tintDrawable(holder.binding.searchButton, R.drawable.icon_player_more, tintColor)
    holder.binding.searchButton.setOnClickListener { v: View? ->
      if (multiChoice.isActive) {
        return@setOnClickListener
      }
      val popupMenu = PopupMenu(holder.itemView.context, holder.binding.searchButton, Gravity.END)
      popupMenu.menuInflater.inflate(R.menu.menu_song_item, popupMenu.menu)
      popupMenu.setOnMenuItemClickListener(
          SongPopupListener((holder.itemView.context as BaseActivity), song, false, ""))
      popupMenu.show()
    }
    if (onItemClickListener != null) {
      holder.binding.root.setOnClickListener { v: View? -> onItemClickListener?.onItemClick(v, holder.adapterPosition) }
      holder.binding.root.setOnLongClickListener { v: View? ->
        onItemClickListener?.onItemLongClick(v, holder.adapterPosition)
        true
      }
    }

    holder.binding.root.isSelected = multiChoice.isPositionCheck(position)
  }

  class SearchResHolder(itemView: View) : BaseViewHolder(itemView) {
    val binding: ItemSearchReulstBinding = ItemSearchReulstBinding.bind(itemView)

  }
}