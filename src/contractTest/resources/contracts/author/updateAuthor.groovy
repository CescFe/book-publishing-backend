package contracts.author

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should update author successfully"

    request {
        method PUT()
        url '/api/v1/authors/477537ff-7e8b-4930-bd41-d7f3589120b1'
        headers {
            contentType(applicationJson())
        }
        body([
                full_name: "Updated J.R.R. Tolkien",
                roles: ["AUTHOR", "ILLUSTRATOR"],
                pseudonym: "Updated Tolkien",
                biography: "Updated English writer and philologist",
                email: "updated.tolkien@example.com",
                website: "https://www.updated-tolkiensociety.org"
        ])
    }

    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body([
                id: "477537ff-7e8b-4930-bd41-d7f3589120b1",
                full_name: "Updated J.R.R. Tolkien",
                roles: ["AUTHOR", "ILLUSTRATOR"],
                pseudonym: "Updated Tolkien",
                biography: "Updated English writer and philologist",
                email: "updated.tolkien@example.com",
                website: "https://www.updated-tolkiensociety.org",
                version: 1,
                created_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*')),
                updated_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*'))
        ])
    }
}
