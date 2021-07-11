package com.leskov.g_shop_test.data.sources.remote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.domain.responses.ImageResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*


class RemoteDataSourceImpl(private val retrofit: Retrofit) : RemoteDataSource {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val user = FirebaseAuth.getInstance().currentUser


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
                            price = document["price"].toString() + " $",
                            images = document["images"] as? List<String> ?: listOf(),
                            user_id = document["user_id"].toString()
                        )
                    )
                }
                it.onSuccess(list)
            }
            .addOnFailureListener { exeption ->
                it.onError(exeption)
            }
    }

    override fun getUserAdverts(): Single<List<AdvertResponse>> = Single.create {
        db.collection("adverts")
            .whereEqualTo("user_id", FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .get()
            .addOnSuccessListener { success ->
               val adverts : MutableList<AdvertResponse> = mutableListOf()
                for (document in success){
                    adverts.add(
                        AdvertResponse(
                            id = document.id,
                            title = document["title"].toString(),
                            description = document["description"].toString(),
                            price = document["price"].toString() + " $",
                            images = document["images"] as? List<String> ?: listOf(),
                            user_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        )
                    )
                }
                it.onSuccess(adverts)
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
                    price = result["price"].toString() + " $",
                    images = result["images"] as? List<String> ?: listOf(),
                    user_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                )
                it.onSuccess(product)
            }
            .addOnFailureListener { exeption ->
                it.onError(exeption)
            }

    }


    override fun getCurrentUserAdvertById(id: String): Single<AdvertResponse> = Single.create {
        db.collection("adverts")
            .document(id)
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

    override fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        images: List<Uri>,
        description: String
    ): Completable = Completable.create {
        db.collection("adverts")
            .document(id)
            .update(
                "title", headline,
                "price", price,
                "images", images,
                "description", description
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    it.onComplete()
                } else {
                    task.exception?.let {
                        throw it
                    }
                }
            }
    }

    override fun uploadImages(images: List<Uri>): Single<List<String>> {
        val storage: FirebaseStorage =
            FirebaseStorage.getInstance("gs://g-shop-test.appspot.com")
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

    override fun loadImage(id: String): Single<List<ImageResponse>> = Single.create { emitter ->
        db.collection("adverts")
            .document(id)
            .get()
            .addOnSuccessListener { complete ->
                val images = ImageResponse(complete["images"] as? List<String> ?: listOf())
                emitter.onSuccess(listOf(images))
            }
            .addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun createUser(user: UserEntity): Completable = Completable.create { emitter ->
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

    override fun deleteAdvert(id: String): Completable = Completable.create { emitter ->
        db.collection("adverts")
            .document(id)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception?.cause!!)
                }
            }
    }

    override fun getUser(): Single<UserEntity> = Single.create { emitter ->
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .get()
            .addOnSuccessListener { document ->
                val userData = UserEntity(
                    name = document["name"].toString(),
                    surName = document["surName"].toString(),
                    phoneNumber = document["phoneNumber"].toString(),
                    city = document["city"].toString(),
                    userDescription = document["userDescription"].toString()
                )
                emitter.onSuccess(userData)
            }
            .addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun loginUser(email: String, password: String): Completable =
        Completable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful)
                            emitter.onComplete()
                        else
                            emitter.onError(it.exception!!)
                    }
                }
//            .addOnFailureListener {
//                if (emitter.isDisposed){
//                    emitter.onError(it)
//                }
//            }
        }

    override fun registerUser(email: String, password: String): Completable =
        Completable.create { emitter ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //userLiveData.postValue(auth.currentUser)
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }

//            .addOnFailureListener {
//                if (emitter.isDisposed){
//                    emitter.onError(it)
//                }
//            }
        }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun deleteUser(email: String, password: String): Completable = Completable.create { emitter ->
        val credential = EmailAuthProvider.getCredential(email, password)
        user?.reauthenticate(credential)
            ?.addOnSuccessListener {
                user.delete()
                emitter.onComplete()
            }
            ?.addOnFailureListener {
                emitter.onError(it)
            }
    }

    override fun logout() = auth.signOut()

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