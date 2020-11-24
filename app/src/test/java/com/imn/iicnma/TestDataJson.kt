package com.imn.iicnma

import org.intellij.lang.annotations.Language

@Language("JSON")
val movieDetailJson = """{
  "adult": false,
  "backdrop_path": "/5UkzNSOK561c2QRy2Zr4AkADzLT.jpg",
  "belongs_to_collection": null,
  "budget": 0,
  "genres": [
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 53,
      "name": "Thriller"
    },
    {
      "id": 18,
      "name": "Drama"
    }
  ],
  "homepage": "http://screen.nsw.gov.au/project/2067",
  "id": 528085,
  "imdb_id": "tt1918734",
  "original_language": "en",
  "original_title": "2067",
  "overview": "A lowly utility worker is called to the future by a mysterious radio signal, he must leave his dying wife to embark on a journey that will force him to face his deepest fears in an attempt to change the fabric of reality and save humankind from its greatest environmental crisis yet.",
  "popularity": 1545.946,
  "poster_path": "/7D430eqZj8y3oVkLFfsWXGRcpEG.jpg",
  "production_companies": [
    {
      "id": 22369,
      "logo_path": null,
      "name": "Arcadia",
      "origin_country": "AU"
    },
    {
      "id": 105250,
      "logo_path": null,
      "name": "KOJO Entertainment",
      "origin_country": "AU"
    },
    {
      "id": 142069,
      "logo_path": null,
      "name": "Elevate Production Finance",
      "origin_country": "AU"
    },
    {
      "id": 107909,
      "logo_path": null,
      "name": "Futurism Studios",
      "origin_country": "US"
    },
    {
      "id": 142070,
      "logo_path": null,
      "name": "Rocketboy",
      "origin_country": "AU"
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "AU",
      "name": "Australia"
    }
  ],
  "release_date": "2020-10-01",
  "revenue": 0,
  "runtime": 114,
  "spoken_languages": [
    {
      "english_name": "English",
      "iso_639_1": "en",
      "name": "English"
    }
  ],
  "status": "Released",
  "tagline": "The fight for the future has begun.",
  "title": "2067",
  "video": false,
  "vote_average": 4.8,
  "vote_count": 357
}
""".trimIndent()

@Language("JSON")
val pagedListJson = """{
  "total_results": 10000,
  "page": 1,
  "total_pages": 500,
  "results": [
    {
      "release_date": "2020-10-01",
      "adult": false,
      "backdrop_path": "/5UkzNSOK561c2QRy2Zr4AkADzLT.jpg",
      "vote_count": 356,
      "genre_ids": [
        878,
        53,
        18
      ],
      "id": 528085,
      "original_language": "en",
      "original_title": "2067",
      "poster_path": "/7D430eqZj8y3oVkLFfsWXGRcpEG.jpg",
      "title": "2067",
      "video": false,
      "vote_average": 4.8,
      "popularity": 1545.946,
      "overview": "A lowly utility worker is called to the future by a mysterious radio signal, he must leave his dying wife to embark on a journey that will force him to face his deepest fears in an attempt to change the fabric of reality and save humankind from its greatest environmental crisis yet."
    },
    {
      "release_date": "2020-10-23",
      "adult": false,
      "backdrop_path": "/86L8wqGMDbwURPni2t7FQ0nDjsH.jpg",
      "id": 724989,
      "genre_ids": [
        28,
        53
      ],
      "overview": "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
      "original_language": "en",
      "original_title": "Hard Kill",
      "poster_path": "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
      "title": "Hard Kill",
      "video": false,
      "vote_average": 5,
      "popularity": 1316.819,
      "vote_count": 163
    },
    {
      "release_date": "2020-08-14",
      "adult": false,
      "backdrop_path": "/wu1uilmhM4TdluKi2ytfz8gidHf.jpg",
      "genre_ids": [
        16,
        14,
        12,
        35,
        10751
      ],
      "id": 400160,
      "overview": "When his best friend Gary is suddenly snatched away, SpongeBob takes Patrick on a madcap mission far beyond Bikini Bottom to save their pink-shelled pal.",
      "original_language": "en",
      "original_title": "The SpongeBob Movie: Sponge on the Run",
      "poster_path": "/jlJ8nDhMhCYJuzOw3f52CP1W8MW.jpg",
      "title": "The SpongeBob Movie: Sponge on the Run",
      "video": false,
      "vote_average": 8,
      "popularity": 1190.392,
      "vote_count": 1505
    },
    {
      "release_date": "2020-07-29",
      "adult": false,
      "backdrop_path": "/2Fk3AB8E9dYIBc2ywJkxk8BTyhc.jpg",
      "genre_ids": [
        28,
        53
      ],
      "id": 524047,
      "overview": "John Garrity, his estranged wife and their young son embark on a perilous journey to find sanctuary as a planet-killing comet hurtles toward Earth. Amid terrifying accounts of cities getting levelled, the Garrity's experience the best and worst in humanity. As the countdown to the global apocalypse approaches zero, their incredible trek culminates in a desperate and last-minute flight to a possible safe haven.",
      "original_language": "en",
      "original_title": "Greenland",
      "poster_path": "/bNo2mcvSwIvnx8K6y1euAc1TLVq.jpg",
      "title": "Greenland",
      "video": false,
      "vote_average": 7.1,
      "popularity": 1081.895,
      "vote_count": 612
    }
  ]
}
""".trimIndent()