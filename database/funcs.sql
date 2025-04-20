CREATE OR REPLACE FUNCTION get_employees(
    _full_name     text    DEFAULT NULL,
    _position_ids  int[]   DEFAULT NULL,
    _genders       text[]  DEFAULT NULL
) RETURNS SETOF employees
    LANGUAGE sql STABLE AS
$$
SELECT e.*
FROM employees e
WHERE
    (_full_name    IS NULL OR e.full_name   ILIKE '%'||_full_name||'%')
  AND (_position_ids IS NULL OR cardinality(_position_ids)=0 OR e.position_id = ANY(_position_ids))
  AND (_genders      IS NULL OR cardinality(_genders)=0 OR e.gender       = ANY(_genders));
$$;
