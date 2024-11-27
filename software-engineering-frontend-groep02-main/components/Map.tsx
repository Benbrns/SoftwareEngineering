import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { Rental } from "@/types";
import { belgianCities } from "./belgianCities";
import { useTranslation } from "next-i18next";

import { Icon } from "leaflet";

interface MapProps {
  rentals: Rental[];
}

const Map: React.FC<MapProps> = ({ rentals }) => {
  const { t } = useTranslation();
  const [mapLoaded, setMapLoaded] = useState(false);
  const [cities, setCities] = useState<{ city: string; street: string }[]>([]);
  const [coordinates, setCoordinates] = useState<
    { city: string; street: string[]; lat: number; lon: number }[]
  >([]);

  const API_KEY = "16e55da3e03987856af07b56382e456e";

  useEffect(() => {
    // Simulate dynamic loading after a delay
    console.log(rentals);
    const timer = setTimeout(() => {
      setMapLoaded(true);
    }, 500); // 1 second delay
    return () => clearTimeout(timer);
  }, []);

  useEffect(() => {
    const extractCitiesAndStreets = () => {
      const cityStreetList = rentals.map((rental) => ({
        city: rental.city,
        street: rental.street,
      }));
      setCities(cityStreetList);
      console.log(cityStreetList);
    };

    extractCitiesAndStreets();
  }, [rentals]);

  useEffect(() => {
    const getCoordinates = () => {
      const groupedCoordinates: {
        [key: string]: {
          city: string;
          street: string[];
          lat: number;
          lon: number;
        };
      } = {};

      cities.forEach(({ city, street }) => {
        const cityData = city
          ? belgianCities.find(
              (c) => c.city.toLowerCase() === city.toLowerCase()
            )
          : null;
        if (cityData) {
          const key = `${cityData.lat},${cityData.lon}`;
          if (!groupedCoordinates[key]) {
            groupedCoordinates[key] = {
              city,
              street: [street],
              lat: cityData.lat,
              lon: cityData.lon,
            };
          } else {
            groupedCoordinates[key].street.push(street);
          }
        }
      });

      const newCoordinates = Object.values(groupedCoordinates);
      setCoordinates(newCoordinates);
    };

    if (cities.length > 0) {
      getCoordinates();
    }
  }, [cities]);

  return (
    <div className="map-wrapper z-0">
      {mapLoaded ? (
        <MapContainer
          center={[50.6465573, 4.351697]}
          zoom={8}
          scrollWheelZoom={false}
          className="map-container"
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          {coordinates.map(({ city, street, lat, lon }, index) => (
            <Marker
              key={index}
              position={[lat, lon]}
              icon={
                new Icon({
                  iconUrl: "/img/marker.png",
                  iconSize: [25, 41],
                  iconAnchor: [12, 41],
                })
              }
            >
              <Popup>
                <div>
                  <p>
                    {t("map.city")} {city}
                  </p>
                  <p>
                    {t("map.rentals")} {street.length}
                  </p>
                  <p>{t("map.street")}</p>
                  <ul>
                    {street.map((str, i) => (
                      <li key={i}>{str}</li>
                    ))}
                  </ul>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      ) : (
        <div>{t("map.loading")}</div>
      )}
    </div>
  );
};

export default Map;
