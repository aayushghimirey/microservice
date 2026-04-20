CREATE TABLE variant_snapshot_unit_ids (
    variant_snapshot_id UUID NOT NULL,
    unit_id UUID NOT NULL,

    PRIMARY KEY (variant_snapshot_id, unit_id),

    CONSTRAINT fk_variant_snapshot
        FOREIGN KEY (variant_snapshot_id)
        REFERENCES variant_snapshot(id)
        ON DELETE CASCADE
);