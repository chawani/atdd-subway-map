const METHOD = {
  PUT(data) {
    return {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    };
  },
  DELETE() {
    return {
      method: "DELETE"
    };
  },
  POST(data) {
    return {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    };
  }
};

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(data => data.json());

  const station = {
    get() {
      return request(`/stations`);
    },
    create(data) {
      return request(`/station`, METHOD.POST(data));
    },
    update(data, id) {
      return request(`/station/${id}`, METHOD.PUT(data));
    },
    delete(id) {
      return request(`/station/${id}`, METHOD.DELETE());
    }
  };

  const line = {
    get(id = "") {
      return request(`/lines/${id}`);
    },
    create(data) {
      return request(`/lines`, METHOD.POST(data));
    },
    update(data, id) {
      return fetch(`/lines/${id}`, METHOD.PUT(data));
    },
    delete(id) {
      return fetch(`/lines/${id}`, METHOD.DELETE());
    }
  };

  const edge = {
    get(id) {
      return !id ? request(`/lines/stations`) : request(`/lines/${id}/stations`);
    }
  };

  return {
    station,
    line,
    edge
  };
})();

export default api;
