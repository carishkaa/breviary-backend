package blue.mild.breviary.backend.errors

import blue.mild.breviary.backend.dtos.PayloadDto

/**
 * EntityAlreadyExistsBreviaryException exception.
 */
class EntityAlreadyExistsBreviaryException(message: String, causes: List<Throwable> = listOf(), override val payload: PayloadDto = PayloadDto()) :
    FormFieldErrorInput, DataBreviaryException(message, causes)
