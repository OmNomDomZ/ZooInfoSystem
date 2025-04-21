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
WHERE (_nickname IS NULL OR a.nickname ILIKE '%'||_nickname||'%')
  AND (_animal_type_ids IS NULL OR cardinality(_animal_type_ids)=0 OR a.animal_type_id = ANY(_animal_type_ids))
  AND (_genders IS NULL OR cardinality(_genders)=0 OR a.gender = ANY(_genders))
  AND (_needs_warm_housing IS NULL OR a.needs_warm_housing = _needs_warm_housing)
  AND (_cage_id IS NULL OR cardinality(_cage_id)=0  OR a.cage_id = ANY(_cage_id));
$$;
