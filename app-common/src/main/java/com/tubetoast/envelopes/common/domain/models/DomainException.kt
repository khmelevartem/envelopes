package com.tubetoast.envelopes.common.domain.models

class DomainException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}
