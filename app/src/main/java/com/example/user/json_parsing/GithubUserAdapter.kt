package com.example.user.json_parsing

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.items.view.*


class GithubUserAdapter(private val githubUsers:ArrayList<GithubUser>):RecyclerView.Adapter<GithubUserAdapter.GithubViewHolder>() {
    override fun onBindViewHolder(holder: GithubViewHolder?, position: Int) {
        holder?.bind(githubUsers[position])
    }

    override fun getItemCount() = githubUsers.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)=
            GithubViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.items, parent, false))



    class GithubViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser){
            itemView?.tViewLogin?.text=githubUser.login
            itemView?.tViewScore?.text = githubUser.score.toString()
            itemView?.tViewUrl?.text = githubUser.html_url
            Picasso.get().load(githubUser?.avatar_url).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(itemView?.imageViewProfile);
        }

    }
}

