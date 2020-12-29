package blue.mild.breviary.backend.errors

import blue.mild.breviary.backend.dtos.PayloadDto

/**
 * FormFieldErrorInput interface for exceptions.
 */
interface FormFieldErrorInput {
    val payload: PayloadDto
}
