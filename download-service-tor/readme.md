	sudo nano /etc/docker/daemon.json

	{
	"hosts": ["fd://", "tcp://0.0.0.0:2375"]
	}

	sudo systemctl edit docker.service
 
	[Service]
	ExecStart=
	ExecStart=/usr/sbin/dockerd

	sudo systemctl daemon-reload
	sudo systemctl restart docker.service
 