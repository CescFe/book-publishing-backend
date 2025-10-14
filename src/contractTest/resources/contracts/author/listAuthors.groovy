package contracts.author

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should list authors successfully"

    request {
        method GET()
        urlPath('/api/v1/authors') {
            queryParameters {
                parameter 'page': $(consumer(optional(regex('\\d+'))), producer('1'))
                parameter 'limit': $(consumer(optional(regex('\\d+'))), producer('20'))
            }
        }
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
                data: [
                        [
                                id: "477537ff-7e8b-4930-bd41-d7f3589120b1",
                                full_name: "J.R.R. Tolkien",
                                roles: ["AUTHOR", "ILLUSTRATOR"],
                                pseudonym: "Tolkien",
                                biography: "English writer and philologist",
                                email: "tolkien@example.com",
                                website: "https://www.tolkiensociety.org",
                                version: 1,
                                created_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*')),
                                updated_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*'))
                        ],
                        [
                                id: "12345678-1234-1234-1234-123456789012",
                                full_name: "Minimal Author",
                                roles: ["AUTHOR"],
                                pseudonym: null,
                                biography: null,
                                email: null,
                                website: null,
                                version: 1,
                                created_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*')),
                                updated_at: $(regex('[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*'))
                        ]
                ],
                meta: [
                        total: 2,
                        page: 1,
                        limit: 20,
                        total_pages: 1
                ]
        ])
    }
}
