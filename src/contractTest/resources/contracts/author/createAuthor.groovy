package contracts.author

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should create author successfully"

    request {
        method POST()
        url '/authors'
        headers {
            contentType(applicationJson())
        }
        body([
                fullName: "J.R.R. Tolkien",
                roles: ["AUTHOR", "ILLUSTRATOR"],
                pseudonym: "Tolkien",
                biography: "English writer and philologist",
                email: "tolkien@example.com",
                website: "https://www.tolkiensociety.org"
        ])
    }

    response {
        status CREATED()
        headers {
            contentType(applicationJson())
        }
        body([
                id: $(regex('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')),
                fullName: "J.R.R. Tolkien",
                roles: ["AUTHOR", "ILLUSTRATOR"],
                pseudonym: "Tolkien",
                biography: "English writer and philologist",
                email: "tolkien@example.com",
                website: "https://www.tolkiensociety.org",
                version: 1,
                createdAt: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*')),
                updatedAt: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*'))
        ])
    }
}
