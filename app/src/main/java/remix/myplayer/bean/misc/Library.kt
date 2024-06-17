package remix.myplayer.bean.misc

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import remix.myplayer.App
import remix.myplayer.R
import remix.myplayer.ui.activity.RemoteFragment
import remix.myplayer.ui.fragment.*

@Parcelize
data class Library(
    val tag: Int,
    val order: Int = tag,
    val className: String = when (tag) {
        TAG_SONG -> SongFragment::class.java.name
        TAG_ALBUM -> AlbumFragment::class.java.name
        TAG_ARTIST -> ArtistFragment::class.java.name
        TAG_PLAYLIST -> PlayListFragment::class.java.name
        else -> throw IllegalArgumentException("unknown tag: $tag")
    }) : Parcelable {

    fun isPlayList(): Boolean {
        return className == PlayListFragment::class.java.name
    }

    fun getTitle(): String {
        return App.context.getString(
            when (tag) {
                TAG_SONG -> R.string.tab_song
                TAG_ALBUM -> R.string.tab_album
                TAG_ARTIST -> R.string.tab_artist
                TAG_PLAYLIST -> R.string.tab_playlist
                else -> throw IllegalArgumentException("unknown tag: $tag")
            })
    }

    companion object {
        const val TAG_SONG = 0
        const val TAG_ALBUM = 1
        const val TAG_ARTIST = 2
        const val TAG_PLAYLIST = 3

        fun getDefaultLibrary(): List<Library> {
            return listOf(
                Library(TAG_SONG),
                Library(TAG_ALBUM),
                Library(TAG_ARTIST),
                Library(TAG_PLAYLIST)
            )
        }

        fun getAllLibraryString(context: Context): List<String> {
            return listOf(
                context.resources.getString(R.string.tab_song),
                context.resources.getString(R.string.tab_album),
                context.resources.getString(R.string.tab_artist),
                context.resources.getString(R.string.tab_playlist)
            )
        }
    }
}
