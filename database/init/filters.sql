CREATE OR REPLACE FUNCTION get_employees(
    _full_name text DEFAULT NULL,
    _position_ids int[] DEFAULT NULL,
    _genders text[] DEFAULT NULL
) RETURNS SETOF employees
    LANGUAGE sql
    STABLE AS
$$
SELECT e.*
FROM employees e
WHERE (_full_name IS NULL OR e.full_name ILIKE '%' || _full_name || '%')
  AND (_position_ids IS NULL OR cardinality(_position_ids) = 0 OR e.position_id = ANY (_position_ids))
  AND (_genders IS NULL OR cardinality(_genders) = 0 OR e.gender = ANY (_genders));
$$;

CREATE OR REPLACE FUNCTION get_animals(
    _nickname text DEFAULT NULL,
    _animal_type_ids int[] DEFAULT NULL,
    _genders text[] DEFAULT NULL,
    _needs_warm_housing bool DEFAULT NULL,
    _cage_id int[] DEFAULT NULL
) RETURNS SETOF animals
    LANGUAGE sql
    STABLE AS
$$
SELECT a.*
FROM animals a
WHERE (_nickname IS NULL OR a.nickname ILIKE '%' || _nickname || '%')
  AND (_animal_type_ids IS NULL OR cardinality(_animal_type_ids) = 0 OR a.animal_type_id = ANY (_animal_type_ids))
  AND (_genders IS NULL OR cardinality(_genders) = 0 OR a.gender = ANY (_genders))
  AND (_needs_warm_housing IS NULL OR a.needs_warm_housing = _needs_warm_housing)
  AND (_cage_id IS NULL OR cardinality(_cage_id) = 0 OR a.cage_id = ANY (_cage_id));
$$;

CREATE OR REPLACE FUNCTION get_suppliers(
    _name                       TEXT    DEFAULT NULL,
    _food_ids                   INT[]   DEFAULT NULL,
    _food_type_ids              INT[]   DEFAULT NULL,
    _date_from                  DATE    DEFAULT NULL,
    _date_to                    DATE    DEFAULT NULL,
    _is_produced_internally     BOOLEAN DEFAULT NULL
)
    RETURNS TABLE(
                     id         INT,
                     name       VARCHAR,
                     contacts   TEXT
                 ) LANGUAGE sql STABLE AS $$
SELECT DISTINCT s.id,
                s.name,
                s.contacts
FROM suppliers s
         LEFT JOIN suppliers_food sf ON sf.supplier_id = s.id
         LEFT JOIN food f ON sf.food_id = f.id
WHERE (_name                   IS NULL OR s.name ILIKE '%'||_name||'%')
  AND (_food_ids               IS NULL OR cardinality(_food_ids)=0 OR f.id = ANY(_food_ids))
  AND (_food_type_ids          IS NULL OR cardinality(_food_type_ids)=0 OR f.food_type_id = ANY(_food_type_ids))
  AND (_date_from              IS NULL OR sf.delivery_date >= _date_from)
  AND (_date_to                IS NULL OR sf.delivery_date <= _date_to)
  AND (_is_produced_internally IS NULL OR f.is_produced_internally = _is_produced_internally)
$$;