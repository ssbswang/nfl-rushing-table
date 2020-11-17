import { useState, useEffect } from "react";

/**
 * React hook for GET /endpoint with JSON response body.
 * @param {string} url
 * @returns {{data: Promise<any>,
 *             loading: boolean,
 *             error: any}}
 */
export default function useFetch(url) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const BASE_URL = `${process.env.REACT_APP_.API_BASE_URL}:${process.env.REACT_APP_.API_PORT}`
    fetch(BASE_URL + url, {
      method: "GET",
      headers: {
        'Accept': "application/json"
      }
    })
    .then(response => {
      if (response.ok) {
        return response.json();
      }
      setError(response);
    })
    .then(data => setData(data))
    .catch(err => {
      console.error(err);
      setError(err);
    })
    .finally(() =>
        setLoading(false));
  }, [url]);

  return { data, loading, error };
}