package contracts.author

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should delete author successfully"

    request {
        method DELETE()
        url '/api/v1/authors/477537ff-7e8b-4930-bd41-d7f3589120b1'
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status NO_CONTENT()
    }
}
