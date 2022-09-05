# DLTerm GA API v0.1

by Chun-yien Chang

## API base url

```
http://ccy.nclab.tw:8001/dlterm
```

## Initiation

- Format: `{base url}/:token`

- Method: `POST`

- Data

```json
{
    "latent_size": 10, // Latent code size (required)
    "population_size": 200, // Optional
    "mutation_rate": 0.95, // Optional
    "crossover_rate": 0.95, // Optional
    "elitism_count": 50, // Optional
}
```

- Return

```json
{
  "generation": 0,
  "session_id": "hello",
  "message": "ok",
  "population": [
    {
      "latent_code": [
        -0.3111291297453356,
        0.085502046806012,
        //...
      ],
      "id": "1ef39b1a-8a3d-4093-9bce-d2614935e423"
    },
    {
      "latent_code": [
        2.2158262107729265,
        -0.5061411779978846,
        //...
      ],
      "id": "ad852999-8f3d-47fb-a6a9-4fdb43bf1810"
    },
    // ...
  ]
}
```

## Evolution

- Format: `{base url}/:token` (The token must have been initiated first).

- Method: `POST`

- Data

```json
{
    "eval": [
        1.632689764221031,
        0.7035394199858056,
        1.2071775719979683,
        ...
    ], // Evaluation of each individual, must match population size (required)
}
```

- Return

```json
{
  "generation": 1,
  "session_id": "hello",
  "message": "ok",
  "population": [
    {
      "latent_code": [
        -0.3111291297453356,
        0.085502046806012,
        //...
      ],
      "id": "1ef39b1a-8a3d-4093-9bce-d2614935e423"
    },
    {
      "latent_code": [
        2.2158262107729265,
        -0.5061411779978846,
        //...
      ],
      "id": "ad852999-8f3d-47fb-a6a9-4fdb43bf1810"
    },
    // ...
  ]
}
```
