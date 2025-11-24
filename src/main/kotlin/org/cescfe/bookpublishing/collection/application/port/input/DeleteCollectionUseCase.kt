package org.cescfe.bookpublishing.collection.application.port.input

interface DeleteCollectionUseCase {
    fun execute(command: Command)

    data class Command(
        val collectionId: String,
    )
}
