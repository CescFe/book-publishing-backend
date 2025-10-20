package contracts.author

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should get author successfully"

    request {
        method GET()
        url '/api/v1/authors/477537ff-7e8b-4930-bd41-d7f3589120b1'
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body([
                id: "477537ff-7e8b-4930-bd41-d7f3589120b1",
                full_name: "J.R.R. Tolkien",
                roles: ["AUTHOR", "ILLUSTRATOR"],
                pseudonym: "Tolkien",
                biography: "English writer and philologist",
                email: "tolkien@example.com",
                website: "https://www.tolkiensociety.org",
                created_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*')),
                updated_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*'))
        ])
    }
}
