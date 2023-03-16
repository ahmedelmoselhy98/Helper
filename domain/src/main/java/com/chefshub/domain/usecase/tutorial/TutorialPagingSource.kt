package com.chefshub.domain.usecase.tutorial

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chefshub.data.entity.tutorial.TutorialModel
import com.chefshub.data.repository.tutorial.TutorialRepository


class TutorialPagingSource(private val faqRepository: TutorialRepository) :
    PagingSource<Int, TutorialModel>() {

    var userId: Int? = null

    override fun getRefreshKey(state: PagingState<Int, TutorialModel>): Int? {

        Log.e("getRefreshKey"," getRefreshKey "+state.anchorPosition)
        return state.anchorPosition.apply {
            if (this == null || this == 0) return 1
            else this.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TutorialModel> {
        try {
            val currentPage = params.key ?: 1
            val resp =
                if (userId == null) faqRepository.getTutorials(currentPage)
                else faqRepository.getTutorials(currentPage,userId!!)

            if (resp.code() == 401)
                return LoadResult.Error(Throwable(message = ERROR_API.UNAUTHRIZED))

            val data =
                resp.body()?.data?: ArrayList()

            Log.e("loadloadload"," data "+data)

            return LoadResult.Page(
//                nextKey = null,
                nextKey = if (data.isEmpty()) null else currentPage.plus(1),
                prevKey = if (currentPage == 1) null else currentPage.minus(1),
                data = data
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LoadResult.Error(ex)
        }
    }
}

