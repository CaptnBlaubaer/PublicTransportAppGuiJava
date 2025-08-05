The application is a project that provides information about the public transport system in the MDV region (Mitteldeutscher Verkehrsbund).
It uses publicly available data from: https://www.mdv.de/site/uploads/mdv_gtfs.zip.

The application has a JavaFx GUI that offers the possibility to search for information about a direct route between two stops for a specific time (default is the current time) 
or to get departure/arrival information for a single stop.

In a second version, the application is split into two parts: One contains the database logic and provides a REST api created with Spring to retrieve the data (https://github.com/CaptnBlaubaer/PublicTransportAppRestApiSpring), while the other represents the GUI and uses REST requests (https://github.com/CaptnBlaubaer/PublicTransportAppRestGuiJava).

The .zip file has to be unpacked into the project directory .../src/main/resources/en/apaschold/apabfahrteninfo/data.
The data is stored in an SQL database using the following SQL statements to create the database.

Translated with DeepL.com (free version)

CREATE TABLE `agencies` (
  `agency_id` varchar(6) PRIMARY KEY,
  `agency_name` text NOT NULL,
  `agency_url` text NOT NULL,
  `agency_timezone` text NOT NULL,
  `agency_phone` text NOT NULL,
  `agency_language` text NOT NULL,
  `agency_fare_url` text NOT NULL
)

CREATE TABLE `calendar_dates` (
  `service_id` varchar(6) NOT NULL,
  `date` varchar(8) NOT NULL,
  `exception_type` int(11) NOT NULL
)

CREATE TABLE `routes` (
  `route_id` varchar(12) PRIMARY KEY,
  `agency_id` varchar(6) NOT NULL,
  `route_short_name` text NOT NULL,
  `route_long_name` text NOT NULL,
  `route_type` text NOT NULL
)

CREATE TABLE `stops` (
  `stop_id` varchar(18) PRIMARY KEY,
  `stop_name` text NOT NULL,
  `stop_lat` text NOT NULL,
  `stop_lon` text NOT NULL
)

CREATE TABLE `stop_times` (
  `trip_id` text NOT NULL,
  `arrival_time` text NOT NULL,
  `departure_time` text NOT NULL,
  `stop_id` varchar(18) NOT NULL,
  `stop_sequence` int(11) NOT NULL,
  `pick_up_type` int(11) NOT NULL,
  `drop_off_type` int(11) NOT NULL
)

CREATE TABLE `transfers` (
  `from_stop_id` varchar(18) NOT NULL,
  `to_stop_id` text NOT NULL,
  `transfer_type` int(11) NOT NULL,
  `min_transfer_time_seconds` int(11) NOT NULL
)

CREATE TABLE `trips` (
  `route_id` text NOT NULL,
  `service_id` text NOT NULL,
  `trip_id` int(11) PRIMARY KEY,
  `trip_head_sign` text NOT NULL,
  `trip_short_name` text NOT NULL,
  `direction_id` text DEFAULT NULL
)
