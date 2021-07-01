package com.leskov.g_shop.data.sources.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.leskov.g_shop_test.data.sources.remote.RemoteDataSource
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*

class RemoteDataSourceImpl(private val retrofit: Retrofit) : RemoteDataSource {

    private val db = FirebaseFirestore.getInstance()

    override fun getAdverts(): Single<List<AdvertResponse>> = Single.create {
        db.collection("adverts")
            .get()
            .addOnSuccessListener { result ->
                val list: MutableList<AdvertResponse> = mutableListOf()
                for (document in result) {
                    list.add(
                        AdvertResponse(
                            id = document.id,
                            title = document["title"].toString(),
                            description = document["description"].toString(),
                            price = document["price"].toString(),
                            images = document["images"] as? List<String> ?: listOf()
                        )
                    )
                }
                it.onSuccess(list)
            }
            .addOnFailureListener { exeption ->
                it.onError(exeption)
            }
    }

    override fun getAdvertById(id: String): Single<AdvertResponse> = Single.create {
        db.collection("adverts")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                val product = AdvertResponse(
                    id = result.id,
                    title = result["title"].toString(),
                    description = result["description"].toString(),
                    price = result["price"].toString(),
                    images = result["images"] as? List<String> ?: listOf()
                )
                it.onSuccess(product)
            }
            .addOnFailureListener { exeption ->
                it.onError(exeption)
            }

    }

    override fun createAdvert(advert: AdvertResponse): Completable = Completable.create {
        db.collection("adverts")
            .add(advert)
            .addOnSuccessListener { result ->
                it.onComplete()
            }
            .addOnFailureListener { exeption ->
                it.onError(exeption)
            }
    }

    override fun uploadImages(images: List<Uri>): Single<List<String>> {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://g-shop-535f9.appspot.com")
        val basePath = "image"
        val userFolder = FirebaseAuth.getInstance().currentUser?.uid!!

        return Observable.fromIterable(images)
            .flatMapSingle { uri ->
                Single.create<String> {
                    val imageName = UUID.randomUUID().toString()
                    val newImageReference: StorageReference = storage.reference.child(
                        "$basePath/$userFolder/$imageName"
                    )
                    val uploadTask = newImageReference.putFile(uri)
                    uploadTask.continueWithTask {
                        if (!uploadTask.isSuccessful) {
                            uploadTask.exception?.let {
                                throw it
                            }
                        }
                        newImageReference.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            it.onSuccess(task.result.toString())
                        } else {
                            task.exception?.let {
                                throw it
                            }
                        }
                    }
                }.subscribeOn(Schedulers.io())
            }
            .toList()
    }

    override fun createUser(user: UserEntity): Completable = Completable.create{ emitter ->
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .set(user)
            .addOnSuccessListener {
                emitter.onComplete()
            }
            .addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun getUser(): Single<UserEntity> = Single.create{ emitter ->
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .get()
            .addOnSuccessListener { document ->
                val userData = UserEntity(
                    name = document["name"].toString(),
                    surName = document["surName"].toString(),
                    phoneNumber = document["number"].toString(),
                    city = document["city"].toString(),
                    description = document["document"].toString()
                )
                emitter.onSuccess(userData)
            }
            .addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun updateProfile(
        name: String,
        surName: String,
        city: String,
        email: String,
        phoneNumber: String,
        userDescription: String
    ): Completable = Completable.create { emmiter ->
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .update(
                "name", name,
                "surName", surName,
                "email", email,
                "city", city,
                "phoneNumber", phoneNumber,
                "userDescription", userDescription
            )
            .addOnSuccessListener {
                emmiter.onComplete()
            }
            .addOnFailureListener {
                emmiter.onError(it)
            }
    }
}